begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.converter.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxb
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
import|;
end_import

begin_class
DECL|class|NoopXmlStreamWriterWrapper
specifier|public
class|class
name|NoopXmlStreamWriterWrapper
implements|implements
name|JaxbXmlStreamWriterWrapper
block|{
annotation|@
name|Override
DECL|method|wrapWriter (XMLStreamWriter writer)
specifier|public
name|XMLStreamWriter
name|wrapWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|)
block|{
return|return
name|writer
return|;
block|}
block|}
end_class

end_unit

