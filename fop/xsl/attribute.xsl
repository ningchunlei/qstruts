<?xml version="1.0" encoding="UTF-8" ?>
<!--
 ============================================================
  SimpleDoc to XSL-FO Stylesheet
 ============================================================
  File Name : attribute.xsl
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

<!-- cover -->
<xsl:attribute-set name="cover.title" >
  <xsl:attribute name="padding">5mm</xsl:attribute>
  <xsl:attribute name="space-before">25mm</xsl:attribute>
  <xsl:attribute name="space-before.conditionality">retain</xsl:attribute>
  <xsl:attribute name="space-after">150mm</xsl:attribute>
  <xsl:attribute name="font-size">24pt</xsl:attribute>
  <xsl:attribute name="font-family">SimHei</xsl:attribute>
  <xsl:attribute name="display-align">center</xsl:attribute>

<xsl:attribute name="text-align">center</xsl:attribute>
  <xsl:attribute name="text-align-last">center</xsl:attribute>
  <xsl:attribute name="start-indent"><xsl:choose><xsl:when test="/doc/@lang='en'">18mm</xsl:when><xsl:when test="$lang='en'">18mm</xsl:when><xsl:when test="/doc/@lang = 'ja'">15mm</xsl:when><xsl:when test="$lang = 'ja'">15mm</xsl:when><xsl:otherwise >15mm</xsl:otherwise></xsl:choose></xsl:attribute>
<xsl:attribute name="end-indent"><xsl:choose><xsl:when test="/doc/@lang='en'">18mm</xsl:when><xsl:when test="$lang='en'">18mm</xsl:when><xsl:when test="/doc/@lang = 'ja'">15mm</xsl:when><xsl:when test="$lang = 'ja'">15mm</xsl:when><xsl:otherwise >15mm</xsl:otherwise></xsl:choose></xsl:attribute>
  <xsl:attribute name="width">130mm</xsl:attribute>
  <xsl:attribute name="height">20mm</xsl:attribute>
  <xsl:attribute name="background-color">#EEEEEE</xsl:attribute>
  <xsl:attribute name="border-style">solid</xsl:attribute>
  <xsl:attribute name="border-color">#888888</xsl:attribute>
  <xsl:attribute name="padding-top">5pt</xsl:attribute>
  <xsl:attribute name="padding-bottom">5pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="cover.date" >
  <xsl:attribute name="space-after">5mm</xsl:attribute>
  <xsl:attribute name="font-size">12pt</xsl:attribute>
  <xsl:attribute name="font-family">SimHei</xsl:attribute>
  <xsl:attribute name="text-align">center</xsl:attribute>
  <xsl:attribute name="text-align-last">center</xsl:attribute>
  <xsl:attribute name="width">160mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="cover.author" >
  <xsl:attribute name="font-size">14pt</xsl:attribute>
  <xsl:attribute name="font-family">SimHei</xsl:attribute>
  <xsl:attribute name="text-align">center</xsl:attribute>
  <xsl:attribute name="text-align-last">center</xsl:attribute>
  <xsl:attribute name="width">160mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="toc" >
  <xsl:attribute name="font-size">13pt</xsl:attribute>
  <xsl:attribute name="font-family">SimHei</xsl:attribute>
  <xsl:attribute name="font-weight">bold</xsl:attribute>
  <xsl:attribute name="space-before">0pt</xsl:attribute>
  <xsl:attribute name="space-after">0pt</xsl:attribute>
  <xsl:attribute name="keep-with-next.within-page">always</xsl:attribute>
</xsl:attribute-set>

<!-- titles -->
<xsl:attribute-set name="h1" >
  <xsl:attribute name="font-size">22pt</xsl:attribute>
  <xsl:attribute name="font-family">SimHei</xsl:attribute>
  <xsl:attribute name="font-weight">bold</xsl:attribute>
  <xsl:attribute name="space-after">14pt</xsl:attribute>
  <xsl:attribute name="break-before">page</xsl:attribute>
  <xsl:attribute name="keep-with-next.within-page">always</xsl:attribute>
  <xsl:attribute name="border-after-style">solid</xsl:attribute>
  <xsl:attribute name="border-after-width">2pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="h2" >
  <xsl:attribute name="font-size">16pt</xsl:attribute>
  <xsl:attribute name="font-family">SimHei</xsl:attribute>
  <xsl:attribute name="font-weight">bold</xsl:attribute>
  <xsl:attribute name="space-before">19pt</xsl:attribute>
  <xsl:attribute name="space-after">5pt</xsl:attribute>
  <xsl:attribute name="keep-with-next.within-page">always</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="h3" >
  <xsl:attribute name="font-size">13pt</xsl:attribute>
  <xsl:attribute name="font-family">SimHei</xsl:attribute>
  <xsl:attribute name="font-weight">bold</xsl:attribute>
  <xsl:attribute name="space-before">14pt</xsl:attribute>
  <xsl:attribute name="space-after">5pt</xsl:attribute>
  <xsl:attribute name="keep-with-next.within-page">always</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="h4" >
  <xsl:attribute name="font-size">12pt</xsl:attribute>
  <xsl:attribute name="font-family">SimHei</xsl:attribute>
  <xsl:attribute name="font-weight">bold</xsl:attribute>
  <xsl:attribute name="space-before">5pt</xsl:attribute>
  <xsl:attribute name="space-after">5pt</xsl:attribute>
  <xsl:attribute name="keep-with-next.within-page">always</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="h5" >
  <xsl:attribute name="font-size">10pt</xsl:attribute>
  <xsl:attribute name="font-family">SimHei</xsl:attribute>
  <xsl:attribute name="font-weight">bold</xsl:attribute>
  <xsl:attribute name="space-before">3pt</xsl:attribute>
  <xsl:attribute name="space-after">3pt</xsl:attribute>
  <xsl:attribute name="keep-with-next.within-page">always</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="code" >
  <xsl:attribute name="font-family">helvetica, sans-serif, SimHei</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="list.item" >
  <xsl:attribute name="space-before">0.4em</xsl:attribute>
  <xsl:attribute name="space-after">0.4em</xsl:attribute>
  <xsl:attribute name="relative-align">baseline</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="table.data" >
  <xsl:attribute name="table-layout">fixed</xsl:attribute>
  <xsl:attribute name="space-before">10pt</xsl:attribute>
  <xsl:attribute name="space-after">10pt</xsl:attribute>
  <xsl:attribute name="border-style">solid</xsl:attribute>
  <xsl:attribute name="border-width">1pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="table.data.caption" >
  <xsl:attribute name="font-family">sans-serif,SimHei</xsl:attribute>
  <xsl:attribute name="text-align">start</xsl:attribute>
  <xsl:attribute name="space-before">3pt</xsl:attribute>
  <xsl:attribute name="space-after">3pt</xsl:attribute>
  <xsl:attribute name="space-after.precedence">2</xsl:attribute>
  <xsl:attribute name="font-weight">bold</xsl:attribute>
  <xsl:attribute name="keep-with-next.within-page">always</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="table.data.th" >
  <xsl:attribute name="border-style">solid</xsl:attribute>
  <xsl:attribute name="border-width">1pt</xsl:attribute>
  <xsl:attribute name="padding-start">0.3em</xsl:attribute>
  <xsl:attribute name="padding-end">0.2em</xsl:attribute>
  <xsl:attribute name="padding-before">2pt</xsl:attribute>
  <xsl:attribute name="padding-after">2pt</xsl:attribute>
  <xsl:attribute name="font-weight">bold</xsl:attribute>
  <xsl:attribute name="font-size">0.9em</xsl:attribute>
  <xsl:attribute name="text-align">center</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="table.data.td" >
  <xsl:attribute name="border-style">solid</xsl:attribute>
  <xsl:attribute name="border-width">0.5pt</xsl:attribute>
  <xsl:attribute name="padding-start">0.3em</xsl:attribute>
  <xsl:attribute name="padding-end">0.2em</xsl:attribute>
  <xsl:attribute name="padding-before">2pt</xsl:attribute>
  <xsl:attribute name="padding-after">2pt</xsl:attribute>
  <xsl:attribute name="font-size">0.9em</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="dt" >
  <xsl:attribute name="font-weight">bold</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="dd.list" >
  <xsl:attribute name="space-before">0.3em</xsl:attribute>
  <xsl:attribute name="space-after">0.5em</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="dd.block" use-attribute-sets="dd.list">
  <xsl:attribute name="start-indent" >from-parent() + 4em</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="figure.title" >
  <xsl:attribute name="font-family">sans-serif,SimHei</xsl:attribute>
  <xsl:attribute name="text-align">center</xsl:attribute>
  <xsl:attribute name="space-before">3pt</xsl:attribute>
  <xsl:attribute name="space-after">10pt</xsl:attribute>
  <xsl:attribute name="font-weight">bold</xsl:attribute>
  <xsl:attribute name="keep-with-previous.within-page">always</xsl:attribute>
  <xsl:attribute name="font-size">0.8em</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="program.title" >
  <xsl:attribute name="font-family">sans-serif,SimHei</xsl:attribute>
  <xsl:attribute name="text-align">center</xsl:attribute>
  <xsl:attribute name="font-weight">bold</xsl:attribute>
  <xsl:attribute name="keep-with-next.within-page">always</xsl:attribute>
  <xsl:attribute name="space-before">1em</xsl:attribute>
  <xsl:attribute name="space-after">0.5em</xsl:attribute>
  <xsl:attribute name="font-size">0.8em</xsl:attribute>
</xsl:attribute-set>

<!-- body -->

<!-- p-->
<xsl:attribute-set name="p">
  <xsl:attribute name="text-indent">1em</xsl:attribute>
  <xsl:attribute name="space-before">0.6em</xsl:attribute>
  <xsl:attribute name="space-after">0.6em</xsl:attribute>
  <xsl:attribute name="text-align">justify</xsl:attribute>
  <xsl:attribute name="keep-together.within-page">always</xsl:attribute>
</xsl:attribute-set>
<!-- p-->
<xsl:attribute-set name="top.p">
  <xsl:attribute name="text-indent">1em</xsl:attribute>
  <xsl:attribute name="space-before">0.6em</xsl:attribute>
  <xsl:attribute name="space-after">0.6em</xsl:attribute>
  <xsl:attribute name="text-align">justify</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="dd.p">
  <!-- xsl:attribute name="text-indent"--><!--1em</xsl:attribute -->
</xsl:attribute-set>


<xsl:attribute-set name="program">
  <xsl:attribute name="white-space">pre</xsl:attribute>
  <xsl:attribute name="wrap-option">wrap</xsl:attribute>
  <xsl:attribute name="background-color">#EEEEEE</xsl:attribute>
  <xsl:attribute name="font-family">monospace,SimHei</xsl:attribute>
  <xsl:attribute name="font-size">9pt</xsl:attribute>
  <xsl:attribute name="padding">0.5em</xsl:attribute>
  <xsl:attribute name="start-indent">0.5em</xsl:attribute>
  <xsl:attribute name="end-indent">0.5em</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="result" >
  <xsl:attribute name="background-color">ivory</xsl:attribute>
  <xsl:attribute name="border-style">inset</xsl:attribute>
  <xsl:attribute name="border-color">gold</xsl:attribute>
  <xsl:attribute name="margin">10pt</xsl:attribute>
  <xsl:attribute name="space-before">10pt</xsl:attribute>
  <xsl:attribute name="space-after">10pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="note" >
  <xsl:attribute name="font-size">0.9em</xsl:attribute>
<xsl:attribute name="text-indent">0em</xsl:attribute>
<xsl:attribute name="start-indent">0em</xsl:attribute>
<xsl:attribute name="text-align">left</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="div.definitiontable" >
  <xsl:attribute name="space-before">1em</xsl:attribute>
  <xsl:attribute name="space-after">1em</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="div.definitiontable.title" >
  <xsl:attribute name="font-weight">bold</xsl:attribute>
  <xsl:attribute name="keep-with-next.within-page">always</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="div.column" >
  <xsl:attribute name="space-before">1em</xsl:attribute>
  <xsl:attribute name="space-after">1em</xsl:attribute>
  <xsl:attribute name="border-style">solid</xsl:attribute>
  <xsl:attribute name="border-color">gray</xsl:attribute>
  <xsl:attribute name="border-width">1.5pt</xsl:attribute>
  <xsl:attribute name="padding">0.5em</xsl:attribute>
  <xsl:attribute name="start-indent">0.5em</xsl:attribute>
  <xsl:attribute name="end-indent">0.5em</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="div.column.title" >
  <xsl:attribute name="font-weight">bold</xsl:attribute>
  <xsl:attribute name="text-decoration">underline</xsl:attribute>
  <xsl:attribute name="space-after">0.5em</xsl:attribute>
  <xsl:attribute name="font-size">0.8em</xsl:attribute>
  <xsl:attribute name="keep-with-next.within-page">always</xsl:attribute>
</xsl:attribute-set>


<xsl:attribute-set name="div.comment" >
  <xsl:attribute name="color">red</xsl:attribute>
  <xsl:attribute name="background-color">yellow</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="div.fo" >
  <xsl:attribute name="border">solid</xsl:attribute>
  <xsl:attribute name="border-width">thin</xsl:attribute>
  <xsl:attribute name="padding">0.5em</xsl:attribute>
  <xsl:attribute name="start-indent">0.5em</xsl:attribute>
  <xsl:attribute name="end-indent">0.5em</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="div.toc" >
  <xsl:attribute name="border-style">solid</xsl:attribute>
  <xsl:attribute name="border-width">thin</xsl:attribute>
  <xsl:attribute name="border-color">black</xsl:attribute>
  <xsl:attribute name="background-color">#EEEEEE</xsl:attribute>
  <xsl:attribute name="padding">1em</xsl:attribute>
  <xsl:attribute name="font-family">SimHei</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="blockquote" >
  <xsl:attribute name="background-color">gray</xsl:attribute>
  <xsl:attribute name="font-style">italic</xsl:attribute>
  <xsl:attribute name="margin">10pt</xsl:attribute>
  <xsl:attribute name="space-before">10pt</xsl:attribute>
  <xsl:attribute name="space-after">10pt</xsl:attribute>
</xsl:attribute-set>

<!-- inline-->
<xsl:attribute-set name="em" >
  <xsl:attribute name="font-weight">bold</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="q" >
  <xsl:attribute name="font-style">italic</xsl:attribute>
</xsl:attribute-set>
<!-- article mode -->
<xsl:attribute-set name="article.title.ja" >
        <xsl:attribute name="font-size">15pt</xsl:attribute>
  <xsl:attribute name="font-family">Arial,sans-serif,SimHei</xsl:attribute>

<xsl:attribute name="text-align">center</xsl:attribute>
  <xsl:attribute name="text-align-last">center</xsl:attribute>
<xsl:attribute name="font-weight">bold</xsl:attribute>
        </xsl:attribute-set>
<xsl:attribute-set name="article.title.en" >
        <xsl:attribute name="font-size">15pt</xsl:attribute>
  <xsl:attribute name="font-family">Times New Roman,SimHei</xsl:attribute>

<xsl:attribute name="text-align">center</xsl:attribute>
  <xsl:attribute name="text-align-last">center</xsl:attribute>
<xsl:attribute name="font-weight">bold</xsl:attribute>
        </xsl:attribute-set>

<xsl:attribute-set name="article.author.ja" >
  <xsl:attribute name="font-size">12pt</xsl:attribute>
  <xsl:attribute name="font-family">SimHei</xsl:attribute>
  <xsl:attribute name="text-align">center</xsl:attribute>
  <xsl:attribute name="text-align-last">center</xsl:attribute></xsl:attribute-set>

<xsl:attribute-set name="article.author.en" >
  <xsl:attribute name="font-size">12pt</xsl:attribute>
  <xsl:attribute name="font-family">SimHei</xsl:attribute>
  <xsl:attribute name="text-align">center</xsl:attribute>
  <xsl:attribute name="text-align-last">center</xsl:attribute>
    </xsl:attribute-set>
<xsl:attribute-set name="article.abstract.ja" >
<xsl:attribute name="text-indent">1em</xsl:attribute>
  <xsl:attribute name="font-size">10.5pt</xsl:attribute>
  <xsl:attribute name="font-family">Arial, SimHei</xsl:attribute>
  <xsl:attribute name="space-before">1em</xsl:attribute>
<xsl:attribute name="space-after">1em</xsl:attribute>

  </xsl:attribute-set>
<xsl:attribute-set name="article.abstract.en" >
<xsl:attribute name="text-indent">1em</xsl:attribute>
  <xsl:attribute name="font-size">10.5pt</xsl:attribute>
  <xsl:attribute name="font-family">SimHei</xsl:attribute>
  <xsl:attribute name="space-before">1em</xsl:attribute>
<xsl:attribute name="space-after">1em</xsl:attribute>

  </xsl:attribute-set>
<xsl:attribute-set name="a">
<xsl:attribute name="text-decoration">underline</xsl:attribute><xsl:attribute name="color">blue</xsl:attribute></xsl:attribute-set>
<xsl:attribute-set name="underline">
<xsl:attribute name="text-decoration">underline</xsl:attribute></xsl:attribute-set>

<xsl:attribute-set name="index">
<xsl:attribute name="font-size">8pt</xsl:attribute>
<xsl:attribute name="font-family">SimHei</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="thumb-class">
<xsl:attribute name="retrieve-boundary">page</xsl:attribute>
<xsl:attribute name="retrieve-position">last-ending-within-page</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="font">
<xsl:attribute name="font-family">SimHei</xsl:attribute>
</xsl:attribute-set>

</xsl:stylesheet>
