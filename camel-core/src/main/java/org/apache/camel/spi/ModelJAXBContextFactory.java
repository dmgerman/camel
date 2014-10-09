begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
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

begin_comment
comment|/**  * Factory to abstract the creation of the Model's JAXBContext.  *  * @author  */
end_comment

begin_interface
DECL|interface|ModelJAXBContextFactory
specifier|public
interface|interface
name|ModelJAXBContextFactory
block|{
DECL|method|newJAXBContext ()
name|JAXBContext
name|newJAXBContext
parameter_list|()
throws|throws
name|JAXBException
function_decl|;
block|}
end_interface

end_unit

