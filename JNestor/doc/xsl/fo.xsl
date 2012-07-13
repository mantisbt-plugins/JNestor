<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="../../docbook/fo/docbook.xsl"/>
	<xsl:param name="generate.legalnotice.link" select="1"/>
	<xsl:param name="suppress.navigation" select="1"/>
	<xsl:param name="admon.graphics" select="1"/>
	<xsl:param name="admon.graphics.path">gfx/</xsl:param>
	<xsl:param name="toc.section.depth" select="4"/>
  <!-- xsl:param name="ignore.image.scaling" select="0"/ -->
  <xsl:param name="paper.type" select="A4"/>
  <xsl:param name="paper.size.portrait" select="A4"/>
  <xsl:param name="default.image.width" select="640"/>
</xsl:stylesheet>

