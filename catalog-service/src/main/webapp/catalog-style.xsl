<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
                exclude-result-prefixes="wsdl xsd soap">

  <xsl:output method="html" indent="yes"/>

  <xsl:template match="/">
    <html>
      <head>
        <meta charset="utf-8"/>
        <title>WSDL: <xsl:value-of select="//wsdl:definitions/@name"/></title>
        <style>
          body{font-family:Segoe UI,Arial,Helvetica,sans-serif;margin:20px}
          h1,h2{color:#2b6ea3}
          pre{background:#f6f8fa;padding:8px;border-radius:4px}
          table{border-collapse:collapse;margin-bottom:16px;width:100%}
          th,td{border:1px solid #ddd;padding:8px;text-align:left}
          th{background:#f0f4f8}
        </style>
      </head>
      <body>
        <h1>WSDL: <xsl:value-of select="//wsdl:definitions/@name"/></h1>

        <h2>Target Namespace</h2>
        <p><xsl:value-of select="//wsdl:definitions/@targetNamespace"/></p>

        <h2>Types</h2>
        <xsl:for-each select="//wsdl:types//xsd:schema">
          <h3>Schema: <xsl:value-of select="@targetNamespace"/></h3>
          <table>
            <tr><th>Element</th><th>Type</th></tr>
            <xsl:for-each select="xsd:element">
              <tr>
                <td><xsl:value-of select="@name"/></td>
                <td><xsl:value-of select="@type"/></td>
              </tr>
            </xsl:for-each>
          </table>
        </xsl:for-each>

        <h2>Messages</h2>
        <table>
          <tr><th>Name</th><th>Part / Element</th></tr>
          <xsl:for-each select="//wsdl:message">
            <tr>
              <td><xsl:value-of select="@name"/></td>
              <td>
                <xsl:for-each select="wsdl:part">
                  <div><xsl:value-of select="concat(@name, ' : ', @element)"/></div>
                </xsl:for-each>
              </td>
            </tr>
          </xsl:for-each>
        </table>

        <h2>Port Types & Operations</h2>
        <xsl:for-each select="//wsdl:portType">
          <h3><xsl:value-of select="@name"/></h3>
          <table>
            <tr><th>Operation</th><th>Input</th><th>Output</th></tr>
            <xsl:for-each select="wsdl:operation">
              <tr>
                <td><xsl:value-of select="@name"/></td>
                <td><xsl:value-of select="wsdl:input/@message"/></td>
                <td><xsl:value-of select="wsdl:output/@message"/></td>
              </tr>
            </xsl:for-each>
          </table>
        </xsl:for-each>

        <h2>Bindings</h2>
        <table>
          <tr><th>Name</th><th>Type</th><th>Transport / Style</th></tr>
          <xsl:for-each select="//wsdl:binding">
            <tr>
              <td><xsl:value-of select="@name"/></td>
              <td><xsl:value-of select="@type"/></td>
              <td>
                <xsl:value-of select="wsdl:binding/@transport"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="wsdl:binding/@style"/>
              </td>
            </tr>
          </xsl:for-each>
        </table>

        <h2>Service</h2>
        <xsl:for-each select="//wsdl:service">
          <h3><xsl:value-of select="@name"/></h3>
          <table>
            <tr><th>Port</th><th>Binding</th><th>Address</th></tr>
            <xsl:for-each select="wsdl:port">
              <tr>
                <td><xsl:value-of select="@name"/></td>
                <td><xsl:value-of select="@binding"/></td>
                <td><xsl:value-of select="soap:address/@location"/></td>
              </tr>
            </xsl:for-each>
          </table>
        </xsl:for-each>

        <h2>Raw WSDL</h2>
        <pre><xsl:copy-of select="//wsdl:definitions"/></pre>
      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>
