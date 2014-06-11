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
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|CamelContext
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
name|CamelException
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
name|Endpoint
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
name|impl
operator|.
name|UriEndpointComponent
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
name|spi
operator|.
name|UriParam
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
name|IntrospectionSupport
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

begin_comment
comment|/**  * Represents the component that manages {@link ${name}Endpoint}.  */
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
name|Component
expr|extends
name|UriEndpointComponent
block|{      @
name|UriParam
specifier|private
name|$
block|{
name|name
block|}
name|Configuration
name|configuration
block|;
specifier|private
name|final
name|$
block|{
name|name
block|}
name|ApiCollection
name|collection
operator|=
name|$
block|{
name|name
block|}
name|ApiCollection
operator|.
name|getCollection
argument_list|()
block|;
specifier|public
name|$
block|{
name|name
block|}
name|Component
argument_list|()
block|{
name|super
argument_list|(
name|$
block|{
name|name
block|}
name|Endpoint
operator|.
name|class
argument_list|)
block|;     }
specifier|public
name|$
block|{
name|name
block|}
name|Component
argument_list|(
name|CamelContext
name|context
argument_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|$
block|{
name|name
block|}
name|Endpoint
operator|.
name|class
argument_list|)
block|;     }
specifier|protected
name|Endpoint
name|createEndpoint
argument_list|(
name|String
name|uri
argument_list|,
name|String
name|remaining
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
argument_list|)
throws|throws
name|Exception
block|{
comment|// split remaining path to get API name and method
name|final
name|String
index|[]
name|pathElements
operator|=
name|remaining
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
block|;
name|String
name|apiNameStr
block|;
name|String
name|methodName
block|;
switch|switch
condition|(
name|pathElements
operator|.
name|length
condition|)
block|{
case|case
literal|1
case|:
name|apiNameStr
operator|=
literal|""
expr_stmt|;
name|methodName
operator|=
name|pathElements
index|[
literal|0
index|]
expr_stmt|;
break|break;
case|case
literal|2
case|:
name|apiNameStr
operator|=
name|pathElements
index|[
literal|0
index|]
expr_stmt|;
name|methodName
operator|=
name|pathElements
index|[
literal|1
index|]
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Invalid URI path ["
operator|+
name|remaining
operator|+
literal|"], must be of the format "
operator|+
name|collection
operator|.
name|getApiNames
argument_list|()
operator|+
literal|"/<operation-name>"
argument_list|)
throw|;
block|}
comment|// get API enum from apiName string
name|final
name|$
block|{
name|name
block|}
name|ApiName
name|apiName
expr_stmt|;
end_expr_stmt

begin_try
try|try
block|{
name|apiName
operator|=
name|$
block|{
name|name
block|}
name|ApiName
operator|.
name|fromValue
argument_list|(
name|apiNameStr
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Invalid URI path prefix ["
operator|+
name|remaining
operator|+
literal|"], must be one of "
operator|+
name|collection
operator|.
name|getApiNames
argument_list|()
argument_list|)
throw|;
block|}
end_try

begin_expr_stmt
name|final
name|$
block|{
name|name
block|}
name|Configuration
name|endpointConfiguration
operator|=
name|createEndpointConfiguration
argument_list|(
name|apiName
argument_list|)
expr_stmt|;
end_expr_stmt

begin_decl_stmt
specifier|final
name|Endpoint
name|endpoint
init|=
operator|new
name|$
block|{
name|name
block|}
name|Endpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|apiName
argument_list|,
name|methodName
argument_list|,
name|endpointConfiguration
argument_list|)
decl_stmt|;
end_decl_stmt

begin_comment
comment|// set endpoint property inBody
end_comment

begin_expr_stmt
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
end_expr_stmt

begin_comment
comment|// configure endpoint properties and initialize state
end_comment

begin_expr_stmt
name|endpoint
operator|.
name|configureProperties
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
end_expr_stmt

begin_return
return|return
name|endpoint
return|;
end_return

begin_expr_stmt
unit|}      private
name|$
block|{
name|name
block|}
name|Configuration
name|createEndpointConfiguration
argument_list|(
name|$
block|{
name|name
block|}
name|ApiName
name|name
argument_list|)
throws|throws
name|Exception
block|{
name|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|componentProperties
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
end_expr_stmt

begin_if
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|configuration
argument_list|,
name|componentProperties
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
end_if

begin_comment
comment|// create endpoint configuration with component properties
end_comment

begin_expr_stmt
name|final
name|$
block|{
name|name
block|}
name|Configuration
name|endpointConfiguration
operator|=
name|collection
operator|.
name|getEndpointConfiguration
argument_list|(
name|name
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|endpointConfiguration
argument_list|,
name|componentProperties
argument_list|)
expr_stmt|;
end_expr_stmt

begin_return
return|return
name|endpointConfiguration
return|;
end_return

begin_expr_stmt
unit|}      public
name|$
block|{
name|name
block|}
name|Configuration
name|getConfiguration
argument_list|()
block|{
return|return
name|configuration
return|;
block|}
end_expr_stmt

begin_decl_stmt
specifier|public
name|void
name|setConfiguration
argument_list|(
name|$
block|{
name|name
block|}
name|Configuration
name|configuration
argument_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
end_decl_stmt

unit|}
end_unit

