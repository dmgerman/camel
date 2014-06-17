begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_expr_stmt
unit|#
operator|#
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
expr|#
operator|#
name|Licensed
name|to
name|the
name|Apache
name|Software
name|Foundation
argument_list|(
name|ASF
argument_list|)
name|under
name|one
name|or
name|more
expr|#
operator|#
name|contributor
name|license
name|agreements
operator|.
name|See
name|the
name|NOTICE
name|file
name|distributed
name|with
expr|#
operator|#
name|this
name|work
end_expr_stmt

begin_for
for|for additional information regarding copyright ownership. ## The ASF licenses this file to You under the Apache License
operator|,
name|Version
literal|2.0
expr|#
operator|#
operator|(
name|the
literal|"License"
operator|)
expr_stmt|;
end_for

begin_expr_stmt
name|you
name|may
name|not
name|use
name|this
name|file
name|except
name|in
name|compliance
name|with
expr|#
operator|#
name|the
name|License
operator|.
name|You
name|may
name|obtain
name|a
name|copy
name|of
name|the
name|License
name|at
expr|#
operator|#
expr|#
operator|#
name|http
operator|:
comment|//www.apache.org/licenses/LICENSE-2.0
expr|#
operator|#
expr|#
operator|#
name|Unless
name|required
name|by
name|applicable
name|law
name|or
name|agreed
name|to
name|in
name|writing
operator|,
name|software
expr|#
operator|#
name|distributed
name|under
name|the
name|License
name|is
name|distributed
name|on
name|an
literal|"AS IS"
name|BASIS
operator|,
expr_stmt|#
operator|#
name|WITHOUT
name|WARRANTIES
name|OR
name|CONDITIONS
name|OF
name|ANY
name|KIND
operator|,
name|either
name|express
name|or
name|implied
operator|.
expr|#
operator|#
name|See
name|the
name|License
end_expr_stmt

begin_for
for|for the specific language governing permissions and ## limitations under the License. ## ------------------------------------------------------------------------ package $
block|{
package|package
block|}
end_for

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Producer
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
name|spi
operator|.
name|UriEndpoint
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
name|AbstractApiEndpoint
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
name|ApiMethodPropertiesHelper
import|;
end_import

begin_import
import|import
name|$
block|{
package|package
block|}
end_import

begin_expr_stmt
operator|.
name|api
operator|.
name|$
block|{
name|name
block|}
name|FileHello
expr_stmt|;
end_expr_stmt

begin_import
import|import
name|$
block|{
package|package
block|}
end_import

begin_expr_stmt
operator|.
name|api
operator|.
name|$
block|{
name|name
block|}
name|JavadocHello
expr_stmt|;
end_expr_stmt

begin_import
import|import
name|$
block|{
package|package
block|}
end_import

begin_expr_stmt
operator|.
name|internal
operator|.
name|$
block|{
name|name
block|}
name|ApiCollection
expr_stmt|;
end_expr_stmt

begin_import
import|import
name|$
block|{
package|package
block|}
end_import

begin_expr_stmt
operator|.
name|internal
operator|.
name|$
block|{
name|name
block|}
name|ApiName
expr_stmt|;
end_expr_stmt

begin_import
import|import
name|$
block|{
package|package
block|}
end_import

begin_expr_stmt
operator|.
name|internal
operator|.
name|$
block|{
name|name
block|}
name|PropertiesHelper
expr_stmt|;
end_expr_stmt

begin_comment
comment|/**  * Represents a ${name} endpoint.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"${scheme}"
argument_list|,
name|consumerClass
operator|=
name|$
block|{
name|name
block|}
name|Consumer
operator|.
name|class
argument_list|,
name|consumerPrefix
operator|=
literal|"consumer"
argument_list|)
DECL|class|$
specifier|public
class|class
name|$
block|{
name|name
block|}
end_class

begin_expr_stmt
DECL|class|$
name|Endpoint
expr|extends
name|AbstractApiEndpoint
operator|<
name|$
block|{
name|name
block|}
name|ApiName
operator|,
name|$
block|{
name|name
block|}
name|Configuration
operator|>
block|{
comment|// TODO create and manage API proxy
specifier|private
name|Object
name|apiProxy
block|;
specifier|public
name|$
block|{
name|name
block|}
name|Endpoint
argument_list|(
name|String
name|uri
argument_list|,
name|$
block|{
name|name
block|}
name|Component
name|component
argument_list|,
name|$
block|{
name|name
block|}
name|ApiName
name|apiName
argument_list|,
name|String
name|methodName
argument_list|,
name|$
block|{
name|name
block|}
name|Configuration
name|endpointConfiguration
argument_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|apiName
argument_list|,
name|methodName
argument_list|,
name|$
block|{
name|name
block|}
name|ApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getHelper
argument_list|(
name|apiName
argument_list|)
argument_list|,
name|endpointConfiguration
argument_list|)
block|;      }
specifier|public
name|Producer
name|createProducer
argument_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|$
block|{
name|name
block|}
name|Producer
argument_list|(
name|this
argument_list|)
return|;
block|}
end_expr_stmt

begin_function
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
comment|// make sure inBody is not set for consumers
if|if
condition|(
name|inBody
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Option inBody is not supported for consumer endpoint"
argument_list|)
throw|;
block|}
name|final
name|$
block|{
name|name
block|}
name|Consumer
name|consumer
operator|=
operator|new
name|$
block|{
name|name
block|}
name|Consumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
expr_stmt|;
comment|// also set consumer.* properties
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
end_function

begin_annotation
annotation|@
name|Override
end_annotation

begin_expr_stmt
specifier|protected
name|ApiMethodPropertiesHelper
operator|<
name|$
block|{
name|name
block|}
name|Configuration
operator|>
name|getPropertiesHelper
argument_list|()
block|{
return|return
name|$
block|{
name|name
block|}
name|PropertiesHelper
operator|.
name|getHelper
argument_list|()
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|Override
specifier|protected
name|void
name|afterConfigureProperties
parameter_list|()
block|{
comment|// TODO create API proxy, set connection properties, etc.
switch|switch
condition|(
operator|(
name|$
block|{
name|name
block|}
name|ApiName
block|)
function|apiName
end_function

begin_block
unit|)
block|{
case|case
name|HELLO_FILE
case|:
name|apiProxy
operator|=
operator|new
name|$
block|{
name|name
block|}
name|FileHello
argument_list|()
expr_stmt|;
break|break;
case|case
name|HELLO_JAVADOC
case|:
name|apiProxy
operator|=
operator|new
name|$
block|{
name|name
block|}
name|JavadocHello
argument_list|()
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid API name "
operator|+
name|apiName
argument_list|)
throw|;
block|}
end_block

begin_function
unit|}      @
name|Override
specifier|public
name|Object
name|getApiProxy
parameter_list|()
block|{
return|return
name|apiProxy
return|;
block|}
end_function

unit|}
end_unit

