
This style guide is based on [Clean Code: A Handbook of Agile Software Craftsmanship] (http://www.tud.ttu.ee/im/Kaarel.Allik/JOOP/Clean_Code_-_A_Handbook_of_Agile_Software_Craftsmanship.pdf) by Robert C. Martin's. We highly recrement that you read this book, if you haven't read it already.

**Table of contents**  
[Naming] (#naming)  
[Functions] (#functions)  
[Comments] (#comments)  
[Formatting] (#formatting)  

# Naming
Please take your time when choosing names. A good name will save you and others lots of time down the track.

A good name:
- shows it itent;
- doesn't give disinformation;
- is meaningfull distintive;
- can be pronouced;
- can be searched;
- avoids enchoding when ever posible;
- doesn't require the reader to make mental notes;
- are not cute;
- follows conventions:
  - class names are written in [UpperCamelCase] [UpperCamelCase];
  - method names are written in [lowerCamelCase] [lowerCamelCase];
  - constants are written in UPPER_CASE;
  - non-constant members are written in [lowerCamelCase] [lowerCamelCase];
  - parameters names are written in [lowerCamelCase] [lowerCamelCase];
  - local variable names are wrottem om are written in [lowerCamelCase] [lowerCamelCase].
- avoids pun or double meaning;
- follows the "one word per concept rule";
- perfers solution domain words over problem domain words;
- add meaningfull context;
- does not include gratuitous content.

[UpperCamelCase]: https://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s5.3-camel-case
[lowerCamelCase]: https://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s5.3-camel-case

# Functions
Again refining your functions so that furfill there rules takes a lite time, but it will save you and other lots of time down the track.

A good function:
- is small;
- does one only thing;
- has instructions that share the same level of abstraction;
- perfers to use abstract factory over switches;
- uses descriptive names;
- has few parameters (<= 3);
- has no side effects;
- avoids to return an error code;
- does or anwser something, but not both;
- are used to tell a story;
- are used to avoid repeatition;

# Comments
Comments are intented to make your code more understandable.

- Prefer code over comments;
- Do use comments:
  - for legal information;
  - to inform;
  - to explain intent;
  - to clarify code;
  - to include a consequences warning;
  - to include a TODO item;
  - to amplify importance of code;
  - to write Javadoc;
- Remove or rewrite comments when they:
  - can be considered mumbling;
  - providing redundent information;
  - misleads;
  - are mandated;
  - contains a journal;
  - add no value;
  - can be replaced by a function or variable;
  - are banners without legitimit use;
  - help you find the scope;
  - give attribution to someone;
  - comments out code;
  - contain HTML;
  - contain system wide information;
  - contain to moch information;
  - don't explain the related code;
  - function as a header;
  - document private or package accessable methods;

# Formatting
- Files should be small (avgerage: 200; soft max: 500);
- Newspaper: the important stuff goes on top;
- Seperate concepts with a blank space;
- **CONTINUE PAGE 79/110**

--- 

http://www.tud.ttu.ee/im/Kaarel.Allik/JOOP/Clean_Code_-_A_Handbook_of_Agile_Software_Craftsmanship.pdf  
https://google-styleguide.googlecode.com/svn/trunk/javaguide.html  
http://www.oracle.com/technetwork/java/codeconvtoc-136057.html  
http://geosoft.no/development/javastyle.html  
http://www.javaranch.com/style.jsp
