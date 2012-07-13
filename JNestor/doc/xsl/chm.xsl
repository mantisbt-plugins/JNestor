<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="docbook-xsl-1.76.1/htmlhelp/htmlhelp.xsl"/>
	<xsl:param name="generate.legalnotice.link" select="1"/>
	<xsl:param name="suppress.navigation" select="0"/>
	<xsl:param name="admon.graphics" select="1"/>
	<xsl:param name="admon.graphics.path">gfx/</xsl:param>
	<xsl:param name="html.stylesheet" select="docbook.css"/>
	<xsl:param name="toc.section.depth" select="4"/>
  <!-- Taken from http://ds9a.nl/docbook/minimal-page.html -->
  <xsl:param name="use.id.as.filename" select="'1'"/>
  <xsl:template name="system.head.content">
    <link rel="stylesheet" href="docbook.css" type="text/css"/>
  </xsl:template>
</xsl:stylesheet>
