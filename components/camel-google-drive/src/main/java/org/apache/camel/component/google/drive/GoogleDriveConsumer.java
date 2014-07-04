begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.google.drive
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|drive
package|;
end_package

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
name|util
operator|.
name|Map
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
name|Processor
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
name|util
operator|.
name|component
operator|.
name|AbstractApiConsumer
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
name|component
operator|.
name|ApiMethod
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
name|component
operator|.
name|google
operator|.
name|drive
operator|.
name|internal
operator|.
name|GoogleDriveApiName
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|googleapis
operator|.
name|services
operator|.
name|AbstractGoogleClientRequest
import|;
end_import

begin_comment
comment|/**  * The GoogleDrive consumer.  */
end_comment

begin_class
DECL|class|GoogleDriveConsumer
specifier|public
class|class
name|GoogleDriveConsumer
extends|extends
name|AbstractApiConsumer
argument_list|<
name|GoogleDriveApiName
argument_list|,
name|GoogleDriveConfiguration
argument_list|>
block|{
DECL|method|GoogleDriveConsumer (GoogleDriveEndpoint endpoint, Processor processor)
specifier|public
name|GoogleDriveConsumer
parameter_list|(
name|GoogleDriveEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doInvokeMethod (Map<String, Object> properties)
specifier|protected
name|Object
name|doInvokeMethod
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
throws|throws
name|RuntimeCamelException
block|{
name|AbstractGoogleClientRequest
name|request
init|=
operator|(
name|AbstractGoogleClientRequest
operator|)
name|super
operator|.
name|doInvokeMethod
argument_list|(
name|properties
argument_list|)
decl_stmt|;
comment|// TODO set any generic params, like OAuth token, etc.
try|try
block|{
return|return
name|request
operator|.
name|execute
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

