<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template name="log">
        <xsl:param name="title" select="'default'"/>

        <div class="line-result">
            <div class="line header">
                <span>
                    <xsl:attribute name="class">status <xsl:value-of select="@status"/></xsl:attribute>
                    <xsl:call-template name="log-status"/>
                </span>
                <span class="processing-type">&#160;</span>
                <span class="value">&#160;
                    <xsl:if test="$title != 'default'">
                        <xsl:value-of select="$title"/>
                    </xsl:if>
                </span>
                <span class="noshow"> | </span>
                <span class="date startDate">
                    <xsl:value-of select="@startDate"/>
                </span>
                <span class="time elapsedTime">&#160;</span>
            </div>
        </div>

        <ol class="collection none">
            <xsl:attribute name="class">
                <xsl:value-of select="@status"/>
            </xsl:attribute>
            <xsl:apply-templates/>
        </ol>

        <div class="line-result">
            <div class="line footer">
                <span>
                    <xsl:attribute name="class">status <xsl:value-of select="@status"/></xsl:attribute>
                    <xsl:call-template name="log-status"/>
                </span>
                <span class="processing-type">&#160;</span>
                <span class="value">&#160;</span>
                <span class="noshow"> | </span>
                <span class="date startDate">
                    <xsl:value-of select="@startDate"/>
                </span>
                <span> - </span>
                <span class="date endDate">
                    <xsl:value-of select="@endDate"/>
                </span>
                <span class="time elapsedTime">&#160;</span>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="collection">
        <ol class="collection">
            <xsl:apply-templates/>
        </ol>
    </xsl:template>

    <xsl:template match="line">
        <li class="line-result">
            <div>
                <xsl:attribute name="class">line <xsl:value-of select="@even"/></xsl:attribute>

                <span>
                    <xsl:attribute name="class">processing-type <xsl:value-of select="@processingType"/></xsl:attribute>
                    <xsl:value-of select="@processingType"/>
                </span>
                <span class="noshow"> | </span>
                <span>
                    <xsl:attribute name="class">status <xsl:value-of select="@status"/></xsl:attribute>
                    <xsl:call-template name="log-status"/>
                </span>
                <span class="noshow"> | </span>
                <span class="path">
                    <xsl:value-of select="@path"/>
                </span>
                <span class="noshow"> | </span>
                <span class="parameters">
                    <xsl:choose>
                        <xsl:when test="parameters != ''">
                            <xsl:value-of select="parameters"/>
                        </xsl:when>
                        <xsl:otherwise>&#160;</xsl:otherwise>
                    </xsl:choose>
                </span>
                <span class="noshow"> | </span>
                <span class="date startDate">
                    <xsl:value-of select="startDate"/>
                </span>
                <span class="noshow"> | </span>
                <span class="time elapsedTime">+<xsl:value-of select="elapsedTime"/> ms</span>
            </div>
            <xsl:apply-templates select="collection | message | exception" />
        </li>
    </xsl:template>

    <xsl:template match="message">
        <ol class="message">
            <div>
                <xsl:attribute name="class">line <xsl:value-of select="@even"/> message</xsl:attribute>

                <span class="message lable">message</span>
                <span class="noshow"> | </span>
                <span class="message value">
                    <xsl:value-of select="value"/>
                </span>
                <span class="noshow"> | </span>
                <span class="date">
                    <xsl:value-of select="date"/>
                </span>
                <span class="time elapsedTime">&#160;</span>
            </div>
        </ol>
    </xsl:template>

    <xsl:template match="exception">
        <ol class="exception">
            <div>
                <xsl:attribute name="class">line <xsl:value-of select="@even"/> exception</xsl:attribute>

                <span class="exception lable">exception</span>
                <span class="noshow"> | </span>
                <span class="exception value">
                    <xsl:value-of select="value"/>
                </span>
                <span class="noshow"> | </span>
                <span class="date">
                    <xsl:value-of select="date"/>
                </span>
                <span class="time elapsedTime">&#160;</span>
            </div>
        </ol>
    </xsl:template>

    <xsl:template name="log-status">
        <xsl:choose>
            <xsl:when test="@status = 'warning'">
                warn
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="@status"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
