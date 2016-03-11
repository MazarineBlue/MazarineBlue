<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="main.xsl"/>
    <xsl:variable name="lowercase" select="'abcdefghijklmnopqrstuvwxyz_'" />
    <xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ '" />

    <xsl:template match="/">
        <html>
            <head>
                <link rel="stylesheet" type="text/css" href="main.css"/>
                <link rel="stylesheet" type="text/css" href="report.css"/>
                <link rel="stylesheet" type="text/css" href="log.css"/>
            </head>
            <body>
                <xsl:apply-templates/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="report">
        <h1>
            <xsl:value-of select="@name"/>
        </h1>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="summary">
        <h2>Summary</h2>
        <xsl:call-template name="report-table">
            <xsl:with-param name="id">suite</xsl:with-param>
            <xsl:with-param name="divider">Suite / Browsers</xsl:with-param>
            <xsl:with-param name="content" select="suite"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="suites">
        <h2>Suites</h2>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="suites/suite">
        <h3>
            <xsl:attribute name="id">suite-<xsl:value-of select="translate(@name, $uppercase, $lowercase)"/>
            </xsl:attribute>

            Suite: <xsl:value-of select="@name"/>
        </h3>
        <xsl:call-template name="report-table">
            <xsl:with-param name="id">testcase-<xsl:value-of select="translate(@name, $uppercase, $lowercase)"/></xsl:with-param>
            <xsl:with-param name="divider">Testcase / Browsers</xsl:with-param>
            <xsl:with-param name="content" select="testcase"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="tickets">
        <h2>Tickets</h2>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="tickets/suite/testcase">
        <h3>
            <xsl:attribute name="id">testcase-<xsl:value-of select="translate(@suite, $uppercase, $lowercase)"/>-<xsl:value-of select="translate(@testcase, $uppercase, $lowercase)"/>
            </xsl:attribute>

            <xsl:value-of select="@suite"/>&#160;-&#160;
            <xsl:value-of select="@testcase"/>
        </h3>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="ticket">
        <xsl:variable name="platform" select="@platform"/>
        <xsl:for-each select="minilog/collection">
            <xsl:call-template name="log">
                <xsl:with-param name="title">
                    <xsl:value-of select="$platform"/>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="report-table">
        <xsl:param name="id"/>
        <xsl:param name="divider"/>
        <xsl:param name="content"/>
        <table>
            <thead class="header">
                <tr>
                    <xsl:attribute name="class">line header</xsl:attribute>

                    <td class="name">
                        <xsl:value-of select="$divider"/>
                    </td>

                    <xsl:for-each select="all/platform">
                        <td class="status">
                            <xsl:value-of select="@name"/>
                        </td>
                    </xsl:for-each>

                    <td class="status">
                        <span>all</span>
                    </td>
                </tr>
            </thead>

            <tbody>
                <xsl:for-each select="$content">
                    <xsl:call-template name="report-record">
                        <xsl:with-param name="id">
                            <xsl:value-of select="$id"/>
                        </xsl:with-param>

                        <xsl:with-param name="content" select="platform"/>
                    </xsl:call-template>
                </xsl:for-each>
            </tbody>

            <tfoot class="footer">
                <xsl:call-template name="report-record">
                    <xsl:with-param name="content" select="all/platform"/>
                </xsl:call-template>
            </tfoot>
        </table>
    </xsl:template>

    <xsl:template name="report-record">
        <xsl:param name="id" select="'default'"/>
        <xsl:param name="content"/>
        <tr>
            <xsl:attribute name="class">line <xsl:value-of select="@even"/>
            </xsl:attribute>

            <td class="name">
                <xsl:choose>
                    <xsl:when test="$id != 'default'">
                        <a>
                            <xsl:attribute name="href">#<xsl:value-of select="$id"/>-<xsl:value-of select="translate(@name, $uppercase, $lowercase)"/>
                            </xsl:attribute>

                            <xsl:value-of select="@name"/>
                        </a>
                    </xsl:when>
                    <xsl:when test="$id = 'default'">
                        <xsl:value-of select="@name"/>
                    </xsl:when>
                </xsl:choose>
            </td>

            <xsl:for-each select="$content">
                <xsl:call-template name="report-status"/>
            </xsl:for-each>

            <xsl:call-template name="report-status"/>
        </tr>
    </xsl:template>

    <xsl:template name="report-status">
        <td>
            <xsl:attribute name="class">status <xsl:value-of select="@status"/>
            </xsl:attribute>

            <xsl:variable name="status" select="@status"/>
            <xsl:variable name="tag" select="name(..)"/>
            <span>
                <xsl:choose>
                    <xsl:when test="$tag = 'testcase'">
                        <xsl:value-of select="@status"/>
                    </xsl:when>
                    <xsl:when test="$status = 'passed'">
                        <xsl:value-of select="@pass"/> / <xsl:value-of select="@total"/>
                    </xsl:when>
                    <xsl:when test="$status = 'failed'">
                        <xsl:value-of select="@fail"/> / <xsl:value-of select="@total"/>
                    </xsl:when>
                    <xsl:when test="$status = 'unset'">
                        <xsl:value-of select="@skipped"/> / <xsl:value-of select="@total"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="@status"/>
                    </xsl:otherwise>
                </xsl:choose>
            </span>
        </td>
    </xsl:template>
</xsl:stylesheet>
