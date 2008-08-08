begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.spring.handler
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|handler
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|OutputKeys
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
name|Result
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
name|stream
operator|.
name|StreamResult
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
name|builder
operator|.
name|xml
operator|.
name|Namespaces
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
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|RouteType
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
name|model
operator|.
name|RoutesType
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
name|spring
operator|.
name|handler
operator|.
name|CamelNamespaceHandler
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
name|util
operator|.
name|ObjectHelper
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
name|RuntimeCamelException
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
name|RuntimeTransformException
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
name|Document
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
name|Element
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

begin_class
DECL|class|ModelFileGenerator
specifier|public
class|class
name|ModelFileGenerator
extends|extends
name|CamelNamespaceHandler
block|{
DECL|field|DEFAULT_ROOT_ELEMENT_NAME
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_ROOT_ELEMENT_NAME
init|=
literal|"routes"
decl_stmt|;
comment|/**      * Write the specified 'routeTypes' to 'fileName' as XML using JAXB.      */
DECL|method|marshalRoutesUsingJaxb (String fileName, List<RouteType> routeTypes)
specifier|public
name|void
name|marshalRoutesUsingJaxb
parameter_list|(
name|String
name|fileName
parameter_list|,
name|List
argument_list|<
name|RouteType
argument_list|>
name|routeTypes
parameter_list|)
throws|throws
name|IOException
block|{
name|OutputStream
name|outputStream
init|=
name|outputStream
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
try|try
block|{
name|XmlConverter
name|converter
init|=
name|converter
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|converter
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|Element
name|root
init|=
name|doc
operator|.
name|createElement
argument_list|(
name|rootElementName
argument_list|()
argument_list|)
decl_stmt|;
name|root
operator|.
name|setAttribute
argument_list|(
literal|"xmlns"
argument_list|,
name|Namespaces
operator|.
name|DEFAULT_NAMESPACE
argument_list|)
expr_stmt|;
name|doc
operator|.
name|appendChild
argument_list|(
name|root
argument_list|)
expr_stmt|;
for|for
control|(
name|RouteType
name|routeType
range|:
name|routeTypes
control|)
block|{
name|addJaxbElementToNode
argument_list|(
name|root
argument_list|,
name|routeType
argument_list|)
expr_stmt|;
block|}
name|Result
name|result
init|=
operator|new
name|StreamResult
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|outputStream
argument_list|,
name|XmlConverter
operator|.
name|defaultCharset
argument_list|)
argument_list|)
decl_stmt|;
name|copyToResult
argument_list|(
name|converter
argument_list|,
name|doc
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeTransformException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|TransformerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeTransformException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|outputStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns a configured XmlConverter      */
DECL|method|converter ()
specifier|private
name|XmlConverter
name|converter
parameter_list|()
block|{
name|XmlConverter
name|converter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|TransformerFactory
name|transformerFactory
init|=
name|converter
operator|.
name|getTransformerFactory
argument_list|()
decl_stmt|;
name|transformerFactory
operator|.
name|setAttribute
argument_list|(
literal|"indent-number"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|converter
return|;
block|}
comment|/**      * Copies the given input Document into the required result using the provided converter.      */
DECL|method|copyToResult (XmlConverter converter, Document doc, Result result)
specifier|private
name|void
name|copyToResult
parameter_list|(
name|XmlConverter
name|converter
parameter_list|,
name|Document
name|doc
parameter_list|,
name|Result
name|result
parameter_list|)
throws|throws
name|TransformerException
block|{
name|Properties
name|outputProperties
init|=
name|converter
operator|.
name|defaultOutputProperties
argument_list|()
decl_stmt|;
name|outputProperties
operator|.
name|put
argument_list|(
name|OutputKeys
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
literal|"no"
argument_list|)
expr_stmt|;
name|outputProperties
operator|.
name|put
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|converter
operator|.
name|toResult
argument_list|(
name|converter
operator|.
name|toSource
argument_list|(
name|doc
argument_list|)
argument_list|,
name|result
argument_list|,
name|outputProperties
argument_list|)
expr_stmt|;
block|}
comment|/**      * Convert the specified object into XML and add it as a child of 'node' using JAXB.      */
DECL|method|addJaxbElementToNode (Node node, Object jaxbElement)
specifier|private
name|void
name|addJaxbElementToNode
parameter_list|(
name|Node
name|node
parameter_list|,
name|Object
name|jaxbElement
parameter_list|)
block|{
try|try
block|{
name|binder
operator|=
name|getJaxbContext
argument_list|()
operator|.
name|createBinder
argument_list|()
expr_stmt|;
name|binder
operator|.
name|marshal
argument_list|(
name|jaxbElement
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Return the root element name for the list of routes.      */
DECL|method|rootElementName ()
specifier|private
name|String
name|rootElementName
parameter_list|()
block|{
name|XmlRootElement
name|annotation
init|=
operator|(
name|RoutesType
operator|.
name|class
operator|)
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
name|String
name|elementName
init|=
name|annotation
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotNullAndNonEmpty
argument_list|(
name|elementName
argument_list|)
condition|)
block|{
return|return
name|elementName
return|;
block|}
block|}
return|return
name|DEFAULT_ROOT_ELEMENT_NAME
return|;
block|}
comment|/**      * returns an output stream for the filename specified.      */
DECL|method|outputStream (String fileName)
specifier|private
name|OutputStream
name|outputStream
parameter_list|(
name|String
name|fileName
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|File
name|parentFile
init|=
name|file
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|parentFile
operator|!=
literal|null
condition|)
block|{
name|parentFile
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
block|}
return|return
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
return|;
block|}
block|}
end_class

end_unit

