begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.groovy.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|groovy
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|groovy
operator|.
name|util
operator|.
name|XmlSlurper
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|util
operator|.
name|slurpersupport
operator|.
name|GPathResult
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|xml
operator|.
name|StreamingMarkupBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Converter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|StringSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_class
annotation|@
name|Converter
DECL|class|GPathResultConverter
specifier|public
class|class
name|GPathResultConverter
block|{
DECL|field|xmlConverter
specifier|private
specifier|final
name|XmlConverter
name|xmlConverter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
annotation|@
name|Converter
DECL|method|fromString (String input)
specifier|public
name|GPathResult
name|fromString
parameter_list|(
name|String
name|input
parameter_list|)
throws|throws
name|ParserConfigurationException
throws|,
name|SAXException
throws|,
name|IOException
block|{
return|return
operator|new
name|XmlSlurper
argument_list|()
operator|.
name|parseText
argument_list|(
name|input
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|fromStringSource (StringSource input)
specifier|public
name|GPathResult
name|fromStringSource
parameter_list|(
name|StringSource
name|input
parameter_list|)
throws|throws
name|IOException
throws|,
name|SAXException
throws|,
name|ParserConfigurationException
block|{
return|return
name|fromString
argument_list|(
name|input
operator|.
name|getText
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|fromNode (Node input, Exchange exchange)
specifier|public
name|GPathResult
name|fromNode
parameter_list|(
name|Node
name|input
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
throws|,
name|SAXException
throws|,
name|ParserConfigurationException
throws|,
name|TransformerException
block|{
return|return
name|fromString
argument_list|(
name|xmlConverter
operator|.
name|toString
argument_list|(
name|input
argument_list|,
name|exchange
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

