/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mazarineblue.keyworddriven.librarymanager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventService;
import org.mazarineblue.keyworddriven.InterpreterContext;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Sentence;
import org.mazarineblue.keyworddriven.exceptions.KeywordConflictException;
import org.mazarineblue.keyworddriven.exceptions.KeywordNotFoundException;
import org.mazarineblue.keyworddriven.exceptions.LibraryNotFoundException;
import org.mazarineblue.keyworddriven.exceptions.SentenceConflictException;
import org.mazarineblue.keyworddriven.exceptions.SentenceNotFoundException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class LibraryManager {

    private final EventService<Event> eventService;
    private final Map<String, List<Library>> namespaces;
    private final Map<Method, Library> methods;
    private final Map<String, Instruction> keywords;
    private final Map<Pattern, Instruction> sentences;
    private InterpreterContext context;

    public LibraryManager(EventService<Event> eventService) {
        this(eventService, null);
    }

    public LibraryManager(EventService<Event> eventService, InterpreterContext context) {
        namespaces = new HashMap<>(4);
        methods = new HashMap<>(32);
        keywords = new HashMap<>(32);
        sentences = new HashMap<>(1);

        this.eventService = eventService;
        this.context = context;
    }

    public LibraryManager(LibraryManager libraryManager, EventService<Event> eventService) {
        namespaces = new HashMap<>(libraryManager.namespaces);
        methods = new HashMap<>(libraryManager.methods);
        keywords = new HashMap<>(libraryManager.keywords);
        sentences = new HashMap<>(libraryManager.sentences);
 
        this.eventService = eventService;
        for (Library library : libraryManager.getLibraries())
            library.setEvents(eventService);
    }

    @Override
    public String toString() {
        String str = null;
        for (Map.Entry<String, List<Library>> entry : namespaces.entrySet())
            if (str == null)
                str = toString(entry);
            else
                str += ", " + toString(entry);
        return str;
    }

    private String toString(Map.Entry<String, List<Library>> entry) {
        return entry.getKey() + '(' + entry.getValue().size() + ')';
    }

    public Collection<Library> getLibraries() {
        Collection<Library> col = new ArrayList<>(countLibraries());
        for (List<Library> list : namespaces.values())
            col.addAll(list);
        return col;
    }

    public final int countLibraries() {
        int count = 0;
        for (List<Library> list : namespaces.values())
            count += list.size();
        return count;
    }

    public Library[] getLibrariesByNamespace(String namespace) {
        List<Library> libraries = namespaces.get(namespace);
        return libraries.toArray(new Library[libraries.size()]);
    }

    public Library getLibraryByMethod(Method method) {
        return methods.get(method);
    }

    public Instruction getInstructionByPath(String namespace, String keyword) {
        return namespace == null || namespace.isEmpty()
                ? globalSearch(keyword)
                : librarySearch(namespace, keyword);
    }

    private Instruction globalSearch(String keyword) {
        checkForGlobalKeyword(keyword);
        return getInstructionByKeyword(keyword);
    }

    private void checkForGlobalKeyword(String keyword) {
        if (keywords.containsKey(keyword))
            return;
        for (List<Library> libraries : namespaces.values())
            for (Library library : libraries)
                if (library.getKeywords().contains(keyword))
                    return;
        throw new KeywordNotFoundException("." + keyword);
    }

    private Instruction getInstructionByKeyword(String keyword) {
        Instruction instruction = keywords.get(keyword);
        if (instruction != null)
            return instruction;
        throw new KeywordConflictException(keyword);
    }

    private Instruction librarySearch(String namespace, String keyword) {
        checkForNamespace(namespace);
        return getInstructionByKeyword(namespace, keyword);
    }

    private void checkForNamespace(String namespace) {
        if (namespaces.containsKey(namespace) == false)
            throw new LibraryNotFoundException(namespace);
    }

    private Instruction getInstructionByKeyword(String namespace, String keyword) {
        for (Library library : namespaces.get(namespace)) {
            Instruction instruction = library.getInstruction(keyword);
            if (instruction != null)
                return instruction;
        }
        throw new KeywordNotFoundException(namespace + "." + keyword);
    }

    public Instruction getInstructionBySentence(String sentence) {
        for (Entry<Pattern, Instruction> entry : sentences.entrySet()) {
            Pattern pattern = entry.getKey();
            Matcher matcher = pattern.matcher(sentence);
            if (matcher.find() && entry.getValue() != null)
                return entry.getValue();
        }
        throw new SentenceNotFoundException(sentence);
    }

    public Instruction getInstructionBySentence(Pattern pattern) {
        Instruction instruction = sentences.get(pattern);
        if (instruction != null)
            return instruction;
        throw new SentenceNotFoundException(pattern);
    }

    public void register(Collection<Library> libraries) {
        for (Library library : libraries)
            register(library);
    }

    public void register(Library... libraries) {
        for (Library library : libraries) {
            library.setContext(context);
            library.setEvents(eventService);
            registerNamespace(library);
            registerMethods(library);
            registerKeywords(library);
// @TODO complete sentence
//            registerSentences(library);
//            library.setEvents(eventService);
        }
    }

    public void setContext(InterpreterContext context) {
        this.context = context;
    }

    private void registerNamespace(Library library) {
        String namespace = library.getNamespace();
        List<Library> list = namespaces.get(namespace);
        if (list == null) {
            list = new ArrayList<>(4);
            namespaces.put(namespace, list);
        } else
            list = namespaces.get(namespace);
        list.add(library);
    }

    private void registerMethods(Library library) {
        for (Method method : library.getClass().getMethods()) {
            Keyword keyword = method.getAnnotation(Keyword.class);
            if (keyword == null)
                continue;
// @TODO complete sentence
//            if (method.getAnnotationsByType(Sentence.class).length == 0)
//                continue;
            methods.put(method, library);
        }
    }

    private void registerKeywords(Library library) {
        for (Method method : library.getClass().getMethods())
            for (Keyword keyword : method.getAnnotationsByType(Keyword.class))
                registerKeyword(library, keyword.value(), method);
    }

    private void registerKeyword(Library library, String keyword, Method method) {
        Instruction instruction = keywords.containsKey(keyword)
                ? null
                : library.getInstruction(keyword);
        keywords.put(keyword, instruction);
    }

    private void registerSentences(Library library) {
        for (Method method : library.getClass().getMethods()) {
            Instruction instruction = getInstructionFromMethod(method);
            for (Annotation annotation : method.getAnnotationsByType(
                    Sentence.class))
                registerSentence((Sentence) annotation, instruction);
        }
    }

    private Instruction getInstructionFromMethod(Method method) {
        Library library = methods.get(method);
        Annotation[] arr = method.getAnnotationsByType(Keyword.class);
        String keyword = arr.length == 0
                ? null
                : ((Keyword) arr[0]).value();
        return keyword == null
                ? library.getInstruction(keyword)
                : keywords.get(keyword);
    }

    private void registerSentence(Sentence sentence, Instruction instruction) {
        String key = sentence.value();
        Pattern pattern = Pattern.compile(key);
        registerSentence(pattern, instruction);
    }

    private void registerSentence(Pattern sentence, Instruction instruction) {
        if (sentences.containsKey(sentence))
            throw new SentenceConflictException(sentence);
        sentences.put(sentence, instruction);
    }

    public void unregister(Library... libraries) {
        for (Library library : libraries) {
            library.setContext(null);
            library.setEvents(null);
            unregisterNamespace(library);
            unregisterMethods(library);
            unregisterKeywords(library);
            // @TODO complete sentence
        }
    }

    private void unregisterNamespace(Library library) {
        String namespace = library.getNamespace();
        List<Library> list = namespaces.get(namespace);
        if (list == null)
            return;
        list = namespaces.get(namespace);
        list.remove(library);
    }

    private void unregisterMethods(Library library) {
        for (Method method : library.getClass().getMethods())
            methods.remove(method);
    }

    private void unregisterKeywords(Library library) {
        for (Method method : library.getClass().getMethods()) {
            Keyword keyword = method.getAnnotation(Keyword.class);
            if (keyword == null)
                continue;
            unregisterKeyword(library, keyword.value(), method);
        }
    }

    private void unregisterKeyword(Library library, String keyword, Method method) {
        if (keywords.containsKey(keyword) == false)
            return;
        Instruction instruction = keywords.get(keyword);
        if (instruction == null) {
            Library[] arr = getLibrariesByByKeyword(keyword);
            if (arr.length == 1)
                keywords.put(keyword, arr[0].getInstruction(keyword));
        } else
            keywords.remove(keyword);
    }

    final public Library[] getLibrariesByByKeyword(String keyword) {
        List<Library> libraries = new ArrayList<>();
        for (List<Library> list : namespaces.values())
            for (Library library : list)
                if (library.getKeywords().contains(keyword))
                    libraries.add(library);
        return libraries.toArray(new Library[libraries.size()]);
    }

    public void setup(InterpreterContext context) {
        for (List<Library> libraries : namespaces.values())
            for (Library library : libraries) {
                library.setContext(context);
                library.setup();
            }
    }

    public void teardown() {
        for (List<Library> libraries : namespaces.values())
            for (Library library : libraries) {
                library.teardown();
                library.setContext(null);
            }
    }
}
