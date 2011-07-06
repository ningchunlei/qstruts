<?xml version="1.0" encoding="UTF-8" ?>
<!--
 ============================================================
  SimpleDoc to XSL-FO Stylesheet
 ============================================================
  File Name : param.xsl
 ============================================================
  Copyright (c) 2003-2007 Antenna House, Inc. All rights reserved.
  Antenna House is a trademark of Antenna House, Inc.
  URL    : http://www.antennahouse.com/
  E-mail : info@antennahouse.com
 ============================================================
-->
<xsl:stylesheet version="1.0"
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:axf="http://www.antennahouse.com/names/XSL/Extensions">
<!-- Common Control -->
<xsl:param name="toc-make" select="true()"/>
<xsl:param name="cover-make" select="true()"/>
<xsl:param name="toc-level-default" select="3"/>
<xsl:param name="index-make" select="true()" />
<!-- thumb index -->
<xsl:param name="thumb-index" />

<xsl:param name="list-startdist" />
<xsl:param name="list-gap" />
<xsl:param name="dl-startdist" />
<xsl:param name="dl-gap" />
<!-- Paper Size -->
<xsl:param name="paper-width"  />
<xsl:param name="paper-height"  />

<!-- Japanese Version Control(Default) -->
<xsl:param name="list-startdist-default-ja" select="string('2em')"/>
<xsl:param name="list-gap-default-ja" select="string('0.2em')"/>
<xsl:param name="dl-startdist-default-ja" select="string('3cm')"/>
<xsl:param name="dl-gap-default-ja" select="string('0.25cm')"/>
<!-- Paper Size -->
<xsl:param name="paper-width-ja" >210mm</xsl:param>
<xsl:param name="paper-height-ja">297mm</xsl:param>

<!-- English Version Control -->

<xsl:param name="list-startdist-default-en" select="string('2em')"/>
<xsl:param name="list-gap-default-en" select="string('0.5em')"/>
<xsl:param name="dl-startdist-default-en" select="string('3cm')"/>
<xsl:param name="dl-gap-default-en" select="string('0.5cm')"/>
<!-- Paper Size -->
<xsl:param name="paper-width-en" >8.5in</xsl:param>
<xsl:param name="paper-height-en">11in</xsl:param>

<!-- Article Virsion Control -->
<!-- Paper Size -->
<xsl:param name="paper-width-art" >182mm</xsl:param>
<xsl:param name="paper-height-art">257mm</xsl:param>

<!-- 2004 lc tag select-->
<xsl:param name="lang">none</xsl:param>


<xsl:variable name="toc-level-max">
<xsl:choose>
<xsl:when test="not (doc/@toclevel)">
<xsl:value-of select="$toc-level-default"/>
</xsl:when>
<xsl:otherwise>
<xsl:value-of select="number(doc/@toclevel)"/>
</xsl:otherwise>
</xsl:choose>
</xsl:variable>

<xsl:variable name="list-startdist-default">
<!-- param already set -->
<xsl:if test="$list-startdist!=''">
<xsl:value-of select="$list-startdist" />
</xsl:if>
<!-- param not setting -->
<xsl:if test="$list-startdist=''">
<xsl:if test="/doc/@lang='en'">
<xsl:value-of select="$list-startdist-default-en" />
</xsl:if>
<xsl:if test="/doc/@lang = 'ja'">
<xsl:value-of select="$list-startdist-default-ja" />
</xsl:if>
<xsl:if test="not(/doc/@lang)">
<xsl:value-of select="$list-startdist-default-ja" />
</xsl:if>
</xsl:if>
</xsl:variable>

<xsl:variable name="list-gap-default">
<xsl:if test="$list-gap!=''">
<xsl:value-of select="$list-gap" />
</xsl:if>
<xsl:if test="$list-gap=''">
<xsl:if test="/doc/@lang='en'">
<xsl:value-of select="$list-gap-default-en" />
</xsl:if>
<xsl:if test="/doc/@lang = 'ja'">
<xsl:value-of select="$list-gap-default-ja" />
</xsl:if>
<xsl:if test="not(/doc/@lang)">
<xsl:value-of select="$list-gap-default-ja" />
</xsl:if>
</xsl:if>
</xsl:variable>

<xsl:variable name="dl-startdist-default">
<xsl:if test="$dl-startdist!=''">
<xsl:value-of select="$dl-startdist" />
</xsl:if>
<xsl:if test="$dl-startdist=''">
<xsl:if test="/doc/@lang='en'">
<xsl:value-of select="$dl-startdist-default-en" />
</xsl:if>
<xsl:if test="/doc/@lang = 'ja'">
<xsl:value-of select="$dl-startdist-default-ja" />
</xsl:if>
<xsl:if test="not(/doc/@lang)">
<xsl:value-of select="$dl-startdist-default-ja" />
</xsl:if>
</xsl:if>
</xsl:variable>

<xsl:variable name="dl-gap-default">
<xsl:if test="$dl-gap!=''">
<xsl:value-of select="$dl-gap" />
</xsl:if>
<xsl:if test="$dl-gap=''">
<xsl:if test="/doc/@lang='en'">
<xsl:value-of select="$dl-gap-default-en" />
</xsl:if>
<xsl:if test="/doc/@lang = 'ja'">
<xsl:value-of select="$dl-gap-default-ja" />
</xsl:if>
<xsl:if test="not(/doc/@lang)">
<xsl:value-of select="$dl-gap-default-ja" />
</xsl:if>
</xsl:if>
</xsl:variable>

<xsl:variable name="paper-width-default">
<xsl:if test="$paper-width!=''">
<xsl:value-of select="$paper-width" />
</xsl:if>
<xsl:if test="$paper-width=''">
<xsl:if test="/doc/@class = 'article'">
<xsl:value-of select="$paper-width-art" />
</xsl:if>
<xsl:if test="/doc[@class='book'] | doc[@class=''] | doc[not(@class)]">
<xsl:if test="/doc/@lang='en'">
<xsl:value-of select="$paper-width-en" />
</xsl:if>
<xsl:if test="/doc/@lang = 'ja'">
<xsl:value-of select="$paper-width-ja" />
</xsl:if>
<xsl:if test="not(/doc/@lang)">
<xsl:value-of select="$paper-width-ja" />
</xsl:if>

</xsl:if>
</xsl:if>
</xsl:variable>

<xsl:variable name="paper-height-default">
<xsl:if test="$paper-height!=''">
<xsl:value-of select="$paper-height" />
</xsl:if>
<xsl:if test="$paper-height=''">
<xsl:if test="/doc/@class = 'article'">
<xsl:value-of select="$paper-height-art" />
</xsl:if>
<xsl:if test="/doc[@class='book'] | doc[@class=''] | doc[not(@class)]">
<xsl:if test="/doc/@lang='en'">
<xsl:value-of select="$paper-height-en" />
</xsl:if>
<xsl:if test="/doc/@lang = 'ja'">
<xsl:value-of select="$paper-height-ja" />
</xsl:if>
<xsl:if test="not(/doc/@lang)">
<xsl:value-of select="$paper-height-ja" />
</xsl:if>
</xsl:if>
</xsl:if>
</xsl:variable>

<xsl:variable name="thumb">
<xsl:if test="$thumb-index = ''">
<xsl:choose>
	<xsl:when test="/doc/body/part">
	<xsl:text>part</xsl:text></xsl:when>
	<xsl:when test="/doc/body/chapter"><xsl:text>chapter</xsl:text></xsl:when>
	<xsl:when test="/doc/body/section"><xsl:text>section</xsl:text></xsl:when>
</xsl:choose>
</xsl:if>
<xsl:if test="$thumb-index != ''"><xsl:value-of select="$thumb-index" /></xsl:if>
</xsl:variable>


</xsl:stylesheet>