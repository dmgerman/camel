begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.sjms.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_comment
comment|/**  * Strategy for creating Destination's  */
end_comment

begin_interface
DECL|interface|DestinationCreationStrategy
specifier|public
interface|interface
name|DestinationCreationStrategy
block|{
DECL|method|createDestination (Session session, String name, boolean topic)
name|Destination
name|createDestination
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|name
parameter_list|,
name|boolean
name|topic
parameter_list|)
throws|throws
name|JMSException
function_decl|;
DECL|method|createTemporaryDestination (Session session, boolean topic)
name|Destination
name|createTemporaryDestination
parameter_list|(
name|Session
name|session
parameter_list|,
name|boolean
name|topic
parameter_list|)
throws|throws
name|JMSException
function_decl|;
block|}
end_interface

end_unit

