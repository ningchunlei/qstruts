<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" indent="yes" encoding="UTF-8"/>
	<xsl:include href="xsl/param.xsl"/>
	<xsl:include href="xsl/attribute.xsl"/>
	<xsl:strip-space elements="table tr td program"/>
	<!-- Normal Version -->
	<xsl:template match="doc[@class='book'] | doc[@class=''] | doc[not(@class)]">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin="25mm 25mm 25mm 25mm" master-name="PageMaster-TOC">
					<xsl:attribute name="page-height">
						<xsl:value-of select="$paper-height-default"/>
					</xsl:attribute>
					<xsl:attribute name="page-width">
						<xsl:value-of select="$paper-width-default"/>
					</xsl:attribute>
					<fo:region-body margin="0mm 0mm 0mm 0mm"/>
				</fo:simple-page-master>
				<fo:simple-page-master margin="25mm 25mm 25mm 25mm" master-name="PageMaster-Cover">
					<xsl:attribute name="page-height">
						<xsl:value-of select="$paper-height-default"/>
					</xsl:attribute>
					<xsl:attribute name="page-width">
						<xsl:value-of select="$paper-width-default"/>
					</xsl:attribute>
					<fo:region-body margin="0mm 0mm 0mm 0mm"/>
				</fo:simple-page-master>
				<fo:simple-page-master margin="25mm 25mm 25mm 25mm" master-name="PageMaster-index">
					<xsl:attribute name="page-height">
						<xsl:value-of select="$paper-height-default"/>
					</xsl:attribute>
					<xsl:attribute name="page-width">
						<xsl:value-of select="$paper-width-default"/>
					</xsl:attribute>
					<fo:region-body margin="00mm 00mm 00mm 00mm" column-count="2" column-gap="20mm"/>
				</fo:simple-page-master>
				<fo:simple-page-master margin="10mm 00mm 10mm 00mm" master-name="PageMaster-Left">
					<xsl:attribute name="page-height">
						<xsl:value-of select="$paper-height-default"/>
					</xsl:attribute>
					<xsl:attribute name="page-width">
						<xsl:value-of select="$paper-width-default"/>
					</xsl:attribute>
					<fo:region-body margin="15mm 25mm 15mm 25mm"/>
					<fo:region-before region-name="Left-header" extent="10mm" display-align="after"/>
					<fo:region-after region-name="Left-footer" extent="10mm" display-align="before"/>
					<fo:region-start region-name="Left-start" extent="25mm"/>
					<fo:region-end region-name="Left-end" extent="25mm"/>
				</fo:simple-page-master>
				<fo:simple-page-master margin="10mm 00mm 10mm 00mm" master-name="PageMaster-Right">
					<xsl:attribute name="page-height">
						<xsl:value-of select="$paper-height-default"/>
					</xsl:attribute>
					<xsl:attribute name="page-width">
						<xsl:value-of select="$paper-width-default"/>
					</xsl:attribute>
					<fo:region-body margin="15mm 25mm 15mm 25mm"/>
					<fo:region-before region-name="Right-header" extent="10mm" display-align="after"/>
					<fo:region-after region-name="Right-footer" extent="10mm" display-align="before"/>
					<fo:region-start region-name="Right-start" extent="25mm"/>
					<fo:region-end region-name="Right-end" extent="25mm"/>
				</fo:simple-page-master>
				<fo:page-sequence-master master-name="PageMaster">
					<fo:repeatable-page-master-alternatives>
						<fo:conditional-page-master-reference master-reference="PageMaster-Left" odd-or-even="even"/>
						<fo:conditional-page-master-reference master-reference="PageMaster-Right" odd-or-even="odd"/>
					</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
			</fo:layout-master-set>
			
			<xsl:call-template name="page.mark"/>
			<xsl:if test="$cover-make or @cover!='false'">
				<xsl:apply-templates select="head"/>
			</xsl:if>
 			<xsl:if test="$toc-make or @toc!='false'">
				<xsl:call-template name="toc"/>
			</xsl:if>
			<xsl:apply-templates select="body"/>
		</fo:root>
	</xsl:template>
	<xsl:template name="page.mark">
		<xsl:if test="/doc/body/part | /doc/body/chapter | /doc/body/section |  /doc/body/appendix">
			<fo:bookmark-tree>
				<xsl:for-each select="/doc/body/part | /doc/body/chapter | /doc/body/section |  /doc/body/appendix">
					<xsl:call-template name="page.mark.line"/>
				</xsl:for-each>
			</fo:bookmark-tree>
		</xsl:if>
	</xsl:template>

	<xsl:template name="page.mark.line">
		<fo:bookmark internal-destination="{generate-id()}">
			<fo:bookmark-title font-weight="bold" font-family="SimHei"><xsl:value-of select="title"/></fo:bookmark-title>
			<xsl:for-each select="part | chapter | section |  appendix">
				<xsl:call-template name="page.mark.line"/>
			</xsl:for-each>
		</fo:bookmark>
	</xsl:template>

	<xsl:template match="doc/head">
		<fo:page-sequence master-reference="PageMaster-Cover" force-page-count="no-force">
			<fo:flow flow-name="xsl-region-body">
				<fo:block xsl:use-attribute-sets="cover.title">
					<xsl:apply-templates select="/doc/head/title"/>
				</fo:block>
				<xsl:if test="/doc/head/date">
				<fo:block-container xsl:use-attribute-sets="cover.date">
					<xsl:apply-templates select="/doc/head/date"/>
				</fo:block-container>
				</xsl:if>
				<xsl:if test="/doc/head/author">
				<fo:block-container xsl:use-attribute-sets="cover.author">
					<xsl:apply-templates select="/doc/head/author"/>
				</fo:block-container>
				</xsl:if>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
	<xsl:template match="doc/head/title">
		<fo:block>
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<xsl:template match="doc/head/date">
		<fo:block>
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<xsl:template match="doc/head/author">
		<fo:block>
			<xsl:if test="@logo">
				<xsl:call-template name="author.logo.img"/>
			</xsl:if>
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<xsl:template name="author.logo.img">
		<xsl:choose>
			<xsl:when test="@pos='side'">
				<fo:inline space-end="1em">
					<fo:external-graphic src="{@logo}">
						<xsl:if test="@width and @height">
							<xsl:attribute name="content-width">
								<xsl:value-of select="@width"/>
							</xsl:attribute>
							<xsl:attribute name="content-height">
								<xsl:value-of select="@height"/>
							</xsl:attribute>
						</xsl:if>
					</fo:external-graphic>
				</fo:inline>
			</xsl:when>
			<xsl:otherwise>
				<fo:block space-after="1em">
					<fo:external-graphic src="{@logo}">
						<xsl:if test="@width and @height">
							<xsl:attribute name="content-width">
								<xsl:value-of select="@width"/>
							</xsl:attribute>
							<xsl:attribute name="content-height">
								<xsl:value-of select="@height"/>
							</xsl:attribute>
						</xsl:if>
					</fo:external-graphic>
				</fo:block>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="toc">
		<fo:page-sequence master-reference="PageMaster-TOC" force-page-count="no-force">
			<fo:flow flow-name="xsl-region-body">
				<fo:block xsl:use-attribute-sets="div.toc">
					<fo:block xsl:use-attribute-sets="toc" space-after="10pt">
						目录
					</fo:block>
					<xsl:for-each select="//part | //chapter | //section |  //appendix">
						<xsl:call-template name="toc.line"/>
					</xsl:for-each>
					<!-- add index page -->
					<xsl:if test="$index-make or @index!='false'">
						<xsl:if test="//index">
							<fo:block text-align-last="justify" space-before="5pt" font-weight="700">
								<fo:basic-link internal-destination="index-page">
									索引
								</fo:basic-link>
								<fo:leader leader-pattern="dots"/>
								<fo:page-number-citation ref-id="index-page"/>
							</fo:block>
						</xsl:if>
					</xsl:if>
				</fo:block>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
	<xsl:template name="toc.line">
		<xsl:variable name="level" select="count(ancestor-or-self::part | ancestor-or-self::chapter | ancestor-or-self::section | ancestor-or-self::appendix)"/>
		<xsl:if test="$level &lt;= $toc-level-max">
			<fo:block text-align-last="justify">
				<xsl:attribute name="margin-left">
					<xsl:value-of select="($level - 1) * 2"/>
					<xsl:text>em</xsl:text>
				</xsl:attribute>
				<xsl:attribute name="space-before">
					<xsl:choose>
						<xsl:when test="$level=1">4pt</xsl:when>
						<xsl:when test="$level=2">3pt</xsl:when>
						<xsl:when test="$level=3">3pt</xsl:when>
						<xsl:otherwise>1pt</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
				<xsl:attribute name="space-before.conditionality">retain</xsl:attribute>
				<xsl:attribute name="space-after">
					<xsl:choose>
						<xsl:when test="$level=2">3pt</xsl:when>
						<xsl:when test="$level=3">1pt</xsl:when>
						<xsl:otherwise>1pt</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
				<xsl:attribute name="space-after.conditionality">retain</xsl:attribute>
				<xsl:attribute name="font-size">
					<xsl:choose>
						<xsl:when test="$level=1">1em</xsl:when>
						<xsl:otherwise>0.9em</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
				<xsl:attribute name="font-weight">
					<xsl:value-of select="800 - $level * 100"/>
				</xsl:attribute>
				<fo:basic-link internal-destination="{generate-id()}">
					<xsl:value-of select="title"/>
				</fo:basic-link>
				<fo:inline font-weight="normal" font-size="10pt">
					<fo:leader leader-pattern="dots"/>
					<fo:page-number-citation ref-id="{generate-id()}"/>
				</fo:inline>
			</fo:block>
		</xsl:if>
	</xsl:template>
	<xsl:template match="body">
		<fo:page-sequence master-reference="PageMaster" initial-page-number="1">
			<fo:static-content flow-name="Left-header">
				<fo:block font-size="10pt" text-align="center" border-after-width="thin" border-after-style="solid"  xsl:use-attribute-sets="font" >
					<xsl:value-of select="/doc/head/title"/>
				</fo:block>
			</fo:static-content>
			<fo:static-content flow-name="Right-header">
				<fo:block font-size="10pt" text-align="center" border-after-width="thin" border-after-style="solid" xsl:use-attribute-sets="font" >
					<xsl:value-of select="/doc/head/title"/>
				</fo:block>
			</fo:static-content>
			<!-- leader for footnote separator -->
			<fo:static-content flow-name="xsl-footnote-separator">
				<fo:block>
					<fo:leader leader-pattern="rule" rule-thickness="0.5pt" leader-length="33%"/>
				</fo:block>
			</fo:static-content>
			<!-- Left page footer -->
			<fo:static-content flow-name="Left-footer">
				<fo:block font-size="9pt" text-align="outside" xsl:use-attribute-sets="font" >
					<fo:inline font-size="17pt">
						<fo:page-number/>
						<xsl:text>|</xsl:text>
					</fo:inline>
					<!-- retrieve-marker is used to output the section name to left page footer. -->
					<fo:retrieve-marker retrieve-boundary="page-sequence" retrieve-position="first-starting-within-page" retrieve-class-name="section-title"/>
				</fo:block>
			</fo:static-content>
			<!-- right page footer -->
			<fo:static-content flow-name="Right-footer">
				<fo:block font-size="9pt" text-align="outside" xsl:use-attribute-sets="font" >
					<!-- retrieve-marker is used to output the section name to right page footer. -->
					<fo:retrieve-marker retrieve-boundary="page-sequence" retrieve-position="first-starting-within-page" retrieve-class-name="section-title"/>
					<fo:inline font-size="17pt">
						<xsl:text>|</xsl:text>
						<fo:page-number/>
					</fo:inline>
				</fo:block>
			</fo:static-content>
			<fo:flow flow-name="xsl-region-body">
				<fo:block xsl:use-attribute-sets="font" >
					<xsl:apply-templates/>
				</fo:block>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
	<xsl:template match="part | chapter | section | appendix ">
		<fo:block>
			<xsl:if test="local-name() = 'section'">
				<xsl:element name="fo:marker">
					<xsl:attribute name="marker-class-name">section-title</xsl:attribute>
					<xsl:value-of select="title"/>
				</xsl:element>
			</xsl:if>
			<xsl:if test="/doc/@class=not('article')">
				<xsl:call-template name="title.out"/>
			</xsl:if>
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<xsl:template match="part/title | chapter/title | section/title | subsection/title | subsubsection/title |  appendix/title "/>
	<xsl:template match="part/title | chapter/title | section/title | subsection/title | subsubsection/title |  appendix/title " mode="applytitle">
		<!--xsl:apply-templates select=".//index"/-->
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template name="title.out">
		<xsl:variable name="level" select="count(ancestor-or-self::part | ancestor-or-self::chapter | ancestor-or-self::section | ancestor-or-self::appendix )"/>
		<xsl:choose>
			<xsl:when test="$level=1">
				<fo:block xsl:use-attribute-sets="h1" id="{generate-id()}">
					<xsl:call-template name="title.out.sub"/>
					<!--xsl:value-of select="title"/-->
					<xsl:number format="1."/><xsl:apply-templates select="title" mode="applytitle"/>
				</fo:block>
			</xsl:when>
			<xsl:when test="$level=2">
				<fo:block xsl:use-attribute-sets="h2" id="{generate-id()}">
					<xsl:call-template name="title.out.sub"/>
					<!--xsl:value-of select="title"/-->
					<xsl:number format="1." count="doc/body/section"/><xsl:number format="1."/><xsl:apply-templates select="title" mode="applytitle"/>
				</fo:block>
			</xsl:when>
			<xsl:when test="$level=3">
				<fo:block xsl:use-attribute-sets="h3" id="{generate-id()}">
					<xsl:call-template name="title.out.sub"/>
					<!--xsl:value-of select="title"/-->
					<xsl:number format="1." count="/doc/body/section"/><xsl:number format="1." count="doc/body/section/section"/><xsl:number format="1."/><xsl:apply-templates select="title" mode="applytitle"/>
				</fo:block>
			</xsl:when>
			<xsl:when test="$level=4">
				<fo:block xsl:use-attribute-sets="h4" id="{generate-id()}">
					<xsl:call-template name="title.out.sub"/>
					<!--xsl:value-of select="title"/-->
					<xsl:apply-templates select="title" mode="applytitle"/>
				</fo:block>
			</xsl:when>
			<xsl:when test="$level=5">
				<fo:block xsl:use-attribute-sets="h5" id="{generate-id()}">
					<xsl:call-template name="title.out.sub"/>
					<!--xsl:value-of select="title"/-->
					<xsl:apply-templates select="title" mode="applytitle"/>
				</fo:block>
			</xsl:when>
			<xsl:otherwise>
				<fo:block xsl:use-attribute-sets="h5" id="{generate-id()}">
					<xsl:call-template name="title.out.sub"/>
					<!--xsl:value-of select="title"/-->
					<xsl:apply-templates select="title" mode="applytitle"/>
				</fo:block>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="title.out.sub">
		<xsl:if test="@logo">
			<fo:inline space-end="5pt">
				<fo:external-graphic src="{@logo}">
					<xsl:if test="@width and @height">
						<xsl:attribute name="content-width">
							<xsl:apply-templates select="@width"/>
						</xsl:attribute>
						<xsl:attribute name="content-height">
							<xsl:apply-templates select="@height"/>
						</xsl:attribute>
					</xsl:if>
				</fo:external-graphic>
			</fo:inline>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="p">
		<fo:block xsl:use-attribute-sets="p">
			<xsl:if test="@type='continue'">
				<xsl:attribute name="text-indent">inherit</xsl:attribute>
			</xsl:if>
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<xsl:template match="figure">
		<xsl:if test="@caption='before'">
			<fo:block xsl:use-attribute-sets="figure.title">
				<xsl:attribute name="space-before">1em</xsl:attribute>
				<xsl:attribute name="space-after">0.5em</xsl:attribute>
				<xsl:apply-templates select="title"/>
			</fo:block>
		</xsl:if>
		<fo:block text-align="center">
			<fo:external-graphic src="{@src}">
				<xsl:if test="@width">
					<xsl:attribute name="content-width">
						<xsl:value-of select="@width"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="@height">
					<xsl:attribute name="content-height">
						<xsl:value-of select="@height"/>
					</xsl:attribute>
				</xsl:if>
			</fo:external-graphic>
		</fo:block>
		<xsl:if test="(@caption='after') or not(@caption)">
			<fo:block xsl:use-attribute-sets="figure.title">
				<xsl:attribute name="space-before">0.5em</xsl:attribute>
				<xsl:attribute name="space-after">1em</xsl:attribute>
				<xsl:value-of select="title"/>
			</fo:block>
		</xsl:if>
	</xsl:template>
	<xsl:template match="ul">
		<!-- The distance between the list-label and the list-body is decided.  -->
		<xsl:variable name="start-dist-local">
			<xsl:choose>
				<xsl:when test="./@startdist">
					<xsl:value-of select="./@startdist"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$list-startdist-default"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="gap-local">
			<xsl:choose>
				<xsl:when test="./@gap">
					<xsl:value-of select="./@gap"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$list-gap-default"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- fo:list-block generate -->
		<fo:list-block provisional-distance-between-starts="{$start-dist-local}" provisional-label-separation="{$gap-local}">
			<!-- processing child element -->
			<xsl:apply-templates/>
		</fo:list-block>
	</xsl:template>
	<xsl:template match="ul/li">
		<fo:list-item xsl:use-attribute-sets="list.item">
			<!-- list-label generation-->
			<!-- The label character is decided by the type attribute. -->
			<fo:list-item-label end-indent="label-end()">
				<fo:block text-align="end">
					<xsl:choose>
						<xsl:when test="../@type='disc'">
							<xsl:text>●</xsl:text>
						</xsl:when>
						<xsl:when test="../@type='circle'">
							<xsl:text>○</xsl:text>
						</xsl:when>
						<xsl:when test="../@type='square'">
							<xsl:text>□</xsl:text>
						</xsl:when>
						<xsl:when test="../@type='bsquare'">
							<xsl:text>■</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>●</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</fo:block>
			</fo:list-item-label>
			<!-- list-body generation -->
			<fo:list-item-body start-indent="body-start()" text-align="justify">
				<fo:block>
					<xsl:apply-templates/>
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>
	<!-- Template to use image for label-->
	<xsl:template match="ul[substring(@type,1,4)='img:']/li">
		<fo:list-item xsl:use-attribute-sets="list.item">
			<fo:list-item-label end-indent="label-end()">
				<fo:block text-align="end">
					<fo:external-graphic src="{substring-after(../@type,substring(../@type,1,4))}" content-height="1.2em" content-width="1.2em"/>
				</fo:block>
			</fo:list-item-label>
			<fo:list-item-body start-indent="body-start()" text-align="justify">
				<fo:block>
					<xsl:apply-templates/>
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>
	<xsl:template match="ol">
		<xsl:variable name="start-dist-local">
			<xsl:choose>
				<xsl:when test="./@startdist">
					<xsl:value-of select="./@startdist"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$list-startdist-default"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="gap-local">
			<xsl:choose>
				<xsl:when test="./@gap">
					<xsl:value-of select="./@gap"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$list-gap-default"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- fo:list-block generation -->
		<fo:list-block provisional-distance-between-starts="{$start-dist-local}" provisional-label-separation="{$gap-local}">
			<!-- processing child element -->
			<xsl:apply-templates/>
		</fo:list-block>
	</xsl:template>
	<xsl:template match="ol/li">
		<fo:list-item xsl:use-attribute-sets="list.item">
			<!-- list-label generation -->
			<!-- The label format is specified by the type attribute. The default is '1'.-->
			<fo:list-item-label end-indent="label-end()">
				<fo:block text-align="end">
					<xsl:choose>
						<xsl:when test="../@type">
							<xsl:number format="{../@type}"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:number format="1."/>
						</xsl:otherwise>
					</xsl:choose>
				</fo:block>
			</fo:list-item-label>
			<!-- list-body generation-->
			<fo:list-item-body start-indent="body-start()" text-align="justify">
				<fo:block>
					<!-- processing child element-->
					<xsl:apply-templates/>
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>
	<xsl:template match="dl">
		<xsl:choose>
			<xsl:when test="@type='list'">
				<xsl:call-template name="dl.format.list"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="dl.format.block"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="dl.format.block">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template name="dl.format.list">
		<xsl:variable name="start-dist-local">
			<xsl:choose>
				<xsl:when test="./@startdist">
					<xsl:value-of select="./@startdist"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$dl-startdist-default"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="gap-local">
			<xsl:choose>
				<xsl:when test="./@gap">
					<xsl:value-of select="./@gap"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$dl-gap-default"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<fo:list-block provisional-distance-between-starts="{$start-dist-local}" provisional-label-separation="{$gap-local}">
			<xsl:call-template name="process.dl.list"/>
		</fo:list-block>
	</xsl:template>
	<xsl:template name="process.dl.list">
		<xsl:param name="dts" select="/.."/>
		<xsl:param name="dds" select="/.."/>
		<xsl:param name="nodes" select="*"/>
		<xsl:choose>
			<xsl:when test="count($nodes)=0">
				<!-- Data end : processing of data in dts and dds -->
				<xsl:if test="count($dts)&gt;0 or count($dds)&gt;0">
					<fo:list-item xsl:use-attribute-sets="list.item">
						<fo:list-item-label end-indent="label-end()">
							<xsl:apply-templates select="$dts"/>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()">
							<xsl:apply-templates select="$dds"/>
						</fo:list-item-body>
					</fo:list-item>
				</xsl:if>
			</xsl:when>
			<xsl:when test="name($nodes[1])='dd'">
				<!-- 'dd' is memorized in variable 'dds'. -->
				<xsl:call-template name="process.dl.list">
					<xsl:with-param name="dts" select="$dts"/>
					<xsl:with-param name="dds" select="$dds|$nodes[1]"/>
					<xsl:with-param name="nodes" select="$nodes[position()&gt;1]"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="name($nodes[1])='dt'">
				<!-- processing of data in dts and dds -->
				<xsl:if test="count($dts)&gt;0 or count($dds)&gt;0">
					<fo:list-item xsl:use-attribute-sets="list.item">
						<fo:list-item-label end-indent="label-end()">
							<xsl:apply-templates select="$dts"/>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()">
							<xsl:apply-templates select="$dds"/>
						</fo:list-item-body>
					</fo:list-item>
				</xsl:if>
				<!-- 'dt' is memorized in variable 'dts'. -->
				<xsl:call-template name="process.dl.list">
					<xsl:with-param name="dts" select="$nodes[1]"/>
					<xsl:with-param name="nodes" select="$nodes[position()&gt;1]"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<!-- error -->
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="dt">
		<xsl:element name="fo:block" use-attribute-sets="dt">
			<xsl:if test="../@mode='debug'">
				<xsl:attribute name="border-color">blue</xsl:attribute>
				<xsl:attribute name="border-style">dashed</xsl:attribute>
				<xsl:attribute name="border-width">thin</xsl:attribute>
			</xsl:if>
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="dd">
		<xsl:choose>
			<xsl:when test="../@type='list'">
				<xsl:element name="fo:block" use-attribute-sets="dd.list">
					<xsl:if test="../@mode='debug'">
						<xsl:attribute name="border-color">red</xsl:attribute>
						<xsl:attribute name="border-style">solid</xsl:attribute>
						<xsl:attribute name="border-width">thin</xsl:attribute>
					</xsl:if>
					<xsl:apply-templates/>
				</xsl:element>
			</xsl:when>
			<xsl:otherwise>
				<xsl:element name="fo:block" use-attribute-sets="dd.block">
					<xsl:if test="../@mode='debug'">
						<xsl:attribute name="border-color">red</xsl:attribute>
						<xsl:attribute name="border-style">solid</xsl:attribute>
						<xsl:attribute name="border-width">thin</xsl:attribute>
					</xsl:if>
					<xsl:apply-templates/>
				</xsl:element>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="table">
			<xsl:if test="@caption">
				<xsl:attribute name="caption-side">
					<xsl:value-of select="@caption"/>
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="title">
				<fo:table-caption xsl:use-attribute-sets="table.data.caption">
					<fo:block start-indent="0em">
						<xsl:apply-templates select="title"/>
					</fo:block>
				</fo:table-caption>
			</xsl:if>
			<fo:table xsl:use-attribute-sets="table.data" text-align="center">
				<xsl:if test="@layout">
					<xsl:attribute name="table-layout">
						<xsl:value-of select="@layout"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="@width">
					<xsl:attribute name="inline-progression-dimension">
						<xsl:value-of select="@width"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="@border-color">
					<xsl:attribute name="border-color">
						<xsl:value-of select="@border-color"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:apply-templates select="col|thead|tfoot|tbody"/>
			</fo:table>
	</xsl:template>
	<xsl:template match="table/title">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="col">
		<xsl:choose>
			<xsl:when test="@number">
				<fo:table-column column-number="{@number}" column-width="{@width}"/>
			</xsl:when>
			<xsl:otherwise>
				<fo:table-column column-width="{@width}"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="thead">
		<fo:table-header>
			<xsl:apply-templates/>
		</fo:table-header>
	</xsl:template>
	<xsl:template match="tfoot">
		<fo:table-footer>
			<xsl:apply-templates/>
		</fo:table-footer>
	</xsl:template>
	<xsl:template match="tbody">
		<fo:table-body>
			<xsl:apply-templates/>
		</fo:table-body>
	</xsl:template>
	<xsl:template match="tr">
		<xsl:element name="fo:table-row">
			<xsl:choose>
				<xsl:when test="@height">
					<xsl:attribute name="block-progression-dimension">
						<xsl:value-of select="@height"/>
					</xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if test="(ancestor::*)[2]/@rowheight">
						<xsl:attribute name="block-progression-dimension">
							<xsl:value-of select="(ancestor::*)[2]/@rowheight"/>
						</xsl:attribute>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="th">
		<fo:table-cell xsl:use-attribute-sets="table.data.th">
			<xsl:call-template name="cell-span"/>
			<xsl:if test="@valign">
				<xsl:attribute name="display-align">
					<xsl:choose>
						<xsl:when test="@valign = 'middle'">
							<xsl:text>center</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="@valign"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="@border-color">
					<xsl:attribute name="border-color">
						<xsl:value-of select="@border-color"/>
					</xsl:attribute>
			</xsl:if>
			<xsl:if test="@background-color">
					<xsl:attribute name="background-color">
						<xsl:value-of select="@background-color"/>	
					</xsl:attribute>
			</xsl:if>	
			<fo:block>
				<xsl:if test="@align">
					<xsl:attribute name="text-align">
						<xsl:value-of select="@align"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:apply-templates/>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	<xsl:template match="td">
		<fo:table-cell xsl:use-attribute-sets="table.data.td">
			<xsl:call-template name="cell-span"/>
			<xsl:if test="@valign">
				<xsl:attribute name="display-align">
					<xsl:choose>
						<xsl:when test="@valign = 'middle'">
							<xsl:text>center</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="@valign"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="@border-color">
					<xsl:attribute name="border-color">
						<xsl:value-of select="@border-color"/>
					</xsl:attribute>
			</xsl:if>
			<xsl:if test="@background-color">
					<xsl:attribute name="background-color">
						<xsl:value-of select="@background-color"/>	
					</xsl:attribute>
			</xsl:if>	
			<fo:block>
				<xsl:variable name="pos" select="position()"/>
				<xsl:if test="@align">
					<xsl:attribute name="text-align">
						<xsl:value-of select="@align"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:apply-templates/>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	<xsl:template name="cell-span">
		<xsl:if test="@colspan">
			<xsl:attribute name="number-columns-spanned">
				<xsl:value-of select="@colspan"/>
			</xsl:attribute>
		</xsl:if>
		<xsl:if test="@rowspan">
			<xsl:attribute name="number-rows-spanned">
				<xsl:value-of select="@rowspan"/>
			</xsl:attribute>
		</xsl:if>
	</xsl:template>
	<xsl:template match="program">
		<xsl:apply-templates select="title"/>
		<fo:block xsl:use-attribute-sets="program"><xsl:apply-templates select="text()"/></fo:block>
	</xsl:template>
	<xsl:template match="program/title">
		<fo:block xsl:use-attribute-sets="program.title">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<xsl:template match="pre">
		<fo:block xsl:use-attribute-sets="program">
			<xsl:apply-templates select="text()"/>
		</fo:block>
	</xsl:template>
	<xsl:template match="div">
		<fo:block>
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<xsl:template match="div[@class = 'definitiontable']">
		<fo:block xsl:use-attribute-sets="div.definitiontable">
			<fo:block xsl:use-attribute-sets="div.definitiontable.title">
				<xsl:text>【</xsl:text>
				<xsl:value-of select="title"/>
				<xsl:text>】</xsl:text>
			</fo:block>
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<xsl:template match="div[@class = 'definitiontable']/title"/>
	<xsl:template match="div[@class = 'column']">
		<fo:block xsl:use-attribute-sets="div.column">
			<fo:block xsl:use-attribute-sets="div.column.title">
				<xsl:apply-templates select="title"/>
			</fo:block>
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<xsl:template match="div[@class = 'column']/title"/>
	<xsl:template match="div[@class = 'hidden']"/>
	<xsl:template match="div[@class = 'result']">
		<fo:block xsl:use-attribute-sets="result">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<xsl:template match="div[@class = 'comment']">
		<fo:block xsl:use-attribute-sets="div.comment">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<xsl:template match="div[@class = 'fo']">
		<fo:block xsl:use-attribute-sets="div.fo">
			<xsl:if test="@border='none'">
				<xsl:attribute name="border-style">none</xsl:attribute>
			</xsl:if>
			<xsl:apply-templates  />
		</fo:block>
	</xsl:template>
	<xsl:template match="div[@class='fo']/text()">
		<xsl:value-of select="." disable-output-escaping="yes"/>
	</xsl:template>
	<xsl:template match="div[@class = 'fo']/title"/>
	<!--<xsl:template match="div[@class='fo']/*[name() != 'title']">
		<xsl:copy-of select="."/>
	</xsl:template>-->
	<xsl:template match="fo:*">
		<xsl:copy-of select="."/>
	</xsl:template>
	<xsl:template match="a[@href]">
		<fo:basic-link>
			<xsl:if test="starts-with(@href,'#')">
				<xsl:attribute name="internal-destination">
					<xsl:value-of select="substring-after(@href,'#')"/>
				</xsl:attribute>
				<fo:inline xsl:use-attribute-sets="a">
					<xsl:apply-templates/>
				</fo:inline>
			</xsl:if>
			<xsl:if test="starts-with(@href,'#')=false">
				<xsl:attribute name="external-destination">
					<xsl:value-of select="@href"/>
				</xsl:attribute>
				<fo:inline xsl:use-attribute-sets="a">
					<xsl:variable name="anchor-texts">
						<xsl:apply-templates/>
					</xsl:variable>
					<xsl:apply-templates/>
					<xsl:if test="@href!=$anchor-texts">
						<fo:inline>
							<xsl:text>(</xsl:text>
							<xsl:value-of select="@href"/>
							<xsl:text>)</xsl:text>
						</fo:inline>
					</xsl:if>
				</fo:inline>
			</xsl:if>
		</fo:basic-link>
	</xsl:template>
	<xsl:template match="a[@name]">
		<fo:inline id="{@name}">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>
	<xsl:template match="note">
		<fo:footnote>
			<fo:inline baseline-shift="super" font-size="75%">
				<xsl:number level="any" count="//note" format="(1)"/>
			</fo:inline>
			<fo:footnote-body>
				<fo:block xsl:use-attribute-sets="note">
					<fo:list-block provisional-label-separation="2pt" provisional-distance-between-starts="15pt">
						<fo:list-item>
							<fo:list-item-label end-indent="label-end()">
								<fo:block baseline-shift="super" font-size="75%">
									<xsl:number level="any" count="//note" format="(1)"/>
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body start-indent="body-start()">
								<fo:block>
									<xsl:apply-templates/>
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
					</fo:list-block>
				</fo:block>
			</fo:footnote-body>
		</fo:footnote>
	</xsl:template>
	<xsl:template match="span">
		<fo:inline>
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>
	<xsl:template match="b">
		<fo:inline font-weight="bold">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>
	<xsl:template match="i">
		<fo:inline font-style="italic">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>
	<xsl:template match="em">
		<fo:inline xsl:use-attribute-sets="em">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>
	<xsl:template match="underline">
		<fo:inline xsl:use-attribute-sets="underline">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>
	<xsl:template match="icon">
		<fo:inline>
			<fo:external-graphic src="{@src}"/>
		</fo:inline>
	</xsl:template>
	<xsl:template match="code">
		<xsl:choose>
			<xsl:when test="@font">
				<fo:inline font-family="{@font}">
					<xsl:apply-templates/>
				</fo:inline>
			</xsl:when>
			<xsl:otherwise>
				<fo:inline font-family="monospace">
					<xsl:apply-templates/>
				</fo:inline>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="code[@class = 'fo']/text()">
		<xsl:value-of select="."  disable-output-escaping="yes"/>
	</xsl:template>
	<xsl:template match="br">
		<fo:block/>
	</xsl:template>
	<xsl:template match="newpage">
		<fo:block break-before="page"/>
	</xsl:template>
	<xsl:template match="hidden"/>
	<xsl:template match="fo">
		<xsl:value-of select="." disable-output-escaping="yes"/>
	</xsl:template>
	<xsl:template match="lc">
		<xsl:choose>
			<xsl:when test="@xml:lang">
				<xsl:if test="@xml:lang=$lang">
					<xsl:apply-templates/>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- generate reference number of Appendix -->
	<xsl:template match="ref">
		<xsl:variable name="target">
			<xsl:call-template name="get-position">
				<xsl:with-param name="id-value" select="@ref-id"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="parent-title" select="//bib/parent::subsubsection/title | //bib/parent::subsection/title | //bib/parent::section/title"></xsl:variable>
		<fo:basic-link internal-destination="{@ref-id}">
			<fo:inline>
				<xsl:choose>
					<xsl:when test="(/doc/@lang = 'ja') or (/doc/@lang = '') or not(/doc/@lang)">
						<xsl:text>（</xsl:text><xsl:value-of select="$parent-title"/><xsl:text>［</xsl:text>
						<xsl:value-of select="$target"/>
						<xsl:text>］）</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>(</xsl:text><xsl:value-of select="$parent-title"/><xsl:text> [</xsl:text>
						<xsl:value-of select="$target"/>
						<xsl:text>])</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</fo:inline>
		</fo:basic-link>
	</xsl:template>
	<xsl:template name="get-position">
		<xsl:param name="id-value"/>
		<xsl:for-each select="//bib/li">
			<xsl:if test="@id = $id-value">
				<xsl:value-of select="position()"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- generate Appendix list -->
	<xsl:template match="bib">
		<fo:list-block>
			<xsl:apply-templates select="li"/>
		</fo:list-block>
	</xsl:template>
	<xsl:template match="bib/li">
		<fo:list-item id="{@id}" space-after="0.5em">
			<fo:list-item-label end-indent="label-end()">
				<fo:block>
					<xsl:text>[</xsl:text>
					<xsl:value-of select="position()"/>
					<xsl:text>]</xsl:text>
				</fo:block>
			</fo:list-item-label>
			<fo:list-item-body start-indent="body-start()">
				<fo:block>
					<!--xsl:value-of select="."/-->
					<xsl:apply-templates/>
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>
</xsl:stylesheet>


