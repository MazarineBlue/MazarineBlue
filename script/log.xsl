<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="main.xsl"/>

    <xsl:template match="/">
        <html>
            <head>
                <link rel="stylesheet" type="text/css" href="main.css"/>
                <link rel="stylesheet" type="text/css" href="log.css"/>
            </head>
            <body>
                <xsl:apply-templates/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="/collection">
        <xsl:call-template name="log"/>
    </xsl:template>
</xsl:stylesheet>
