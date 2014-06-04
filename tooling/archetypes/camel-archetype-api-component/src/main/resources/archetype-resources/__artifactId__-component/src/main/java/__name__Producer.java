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
name|List
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|AsyncCallback
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
name|impl
operator|.
name|DefaultAsyncProducer
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
name|ExecutorServiceManager
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
name|ThreadPoolProfile
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
name|util
operator|.
name|component
operator|.
name|ApiMethodHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|Constants
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
comment|/**  * The ${name} producer.  */
end_comment

begin_class
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
name|Producer
expr|extends
name|DefaultAsyncProducer
block|{
specifier|private
specifier|static
name|final
specifier|transient
name|Logger
name|LOG
operator|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|$
block|{
name|name
block|}
name|Producer
operator|.
name|class
argument_list|)
block|;
comment|// thread pool executor
specifier|private
specifier|static
name|ExecutorService
name|executorService
block|;
specifier|private
name|$
block|{
name|name
block|}
name|Endpoint
name|endpoint
block|;
specifier|private
name|final
name|$
block|{
name|name
block|}
name|PropertiesHelper
name|propertiesHelper
block|;
specifier|private
name|final
name|ApiMethodHelper
name|methodHelper
block|;
specifier|public
name|$
block|{
name|name
block|}
name|Producer
argument_list|(
name|$
block|{
name|name
block|}
name|Endpoint
name|endpoint
argument_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
block|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
block|;
comment|// cache helpers
name|this
operator|.
name|propertiesHelper
operator|=
name|$
block|{
name|name
block|}
name|PropertiesHelper
operator|.
name|getHelper
argument_list|()
block|;
name|this
operator|.
name|methodHelper
operator|=
name|endpoint
operator|.
name|getMethodHelper
argument_list|()
block|;     }
expr|@
name|Override
specifier|public
name|boolean
name|process
argument_list|(
name|final
name|Exchange
name|exchange
argument_list|,
name|final
name|AsyncCallback
name|callback
argument_list|)
block|{
comment|// properties for method arguments
name|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
block|;
name|propertiesHelper
operator|.
name|getEndpointProperties
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|properties
argument_list|)
block|;
name|propertiesHelper
operator|.
name|getExchangeProperties
argument_list|(
name|exchange
argument_list|,
name|properties
argument_list|)
block|;
comment|// decide which method to invoke
name|final
name|Enum
argument_list|<
name|?
extends|extends
name|ApiMethod
argument_list|>
name|method
operator|=
name|findMethod
argument_list|(
name|exchange
argument_list|,
name|properties
argument_list|)
block|;
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
comment|// synchronous failure
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// create a runnable invocation task to be submitted on a background thread pool
comment|// this way we avoid blocking the current thread for long running methods
name|Runnable
name|invocation
operator|=
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Invoking operation {} with {}"
argument_list|,
operator|(
operator|(
name|ApiMethod
operator|)
name|method
operator|)
operator|.
name|getName
argument_list|()
argument_list|,
name|properties
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// also check whether we need to get Raw JSON
name|Object
name|result
operator|=
name|methodHelper
operator|.
name|invokeMethod
argument_list|(
name|endpoint
operator|.
name|getApiProxy
argument_list|()
argument_list|,
name|method
argument_list|,
name|properties
argument_list|)
expr_stmt|;
end_expr_stmt

begin_comment
comment|// producer returns a single response, even for methods with List return types
end_comment

begin_expr_stmt
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
end_expr_stmt

begin_comment
comment|// copy headers
end_comment

begin_expr_stmt
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
unit|} catch
operator|(
name|Throwable
name|t
operator|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|t
argument_list|)
argument_list|)
block|;                 }
end_expr_stmt

begin_finally
finally|finally
block|{
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
end_finally

begin_empty_stmt
unit|}         }
empty_stmt|;
end_empty_stmt

begin_expr_stmt
name|getExecutorService
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|)
operator|.
name|submit
argument_list|(
name|invocation
argument_list|)
expr_stmt|;
end_expr_stmt

begin_return
return|return
literal|false
return|;
end_return

begin_function
unit|}      private
name|Enum
argument_list|<
name|?
extends|extends
name|ApiMethod
argument_list|>
name|findMethod
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|Enum
argument_list|<
name|?
extends|extends
name|ApiMethod
argument_list|>
name|method
init|=
literal|null
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Enum
argument_list|<
name|?
extends|extends
name|ApiMethod
argument_list|>
argument_list|>
name|candidates
init|=
name|endpoint
operator|.
name|getCandidates
argument_list|()
decl_stmt|;
if|if
condition|(
name|processInBody
argument_list|(
name|exchange
argument_list|,
name|properties
argument_list|)
condition|)
block|{
comment|// filter candidates based on endpoint and exchange properties
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|argNames
init|=
name|properties
operator|.
name|keySet
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Enum
argument_list|<
name|?
extends|extends
name|ApiMethod
argument_list|>
argument_list|>
name|filteredMethods
init|=
name|methodHelper
operator|.
name|filterMethods
argument_list|(
name|candidates
argument_list|,
name|ApiMethodHelper
operator|.
name|MatchType
operator|.
name|SUPER_SET
argument_list|,
name|argNames
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|argNames
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
decl_stmt|;
comment|// get the method to call
if|if
condition|(
name|filteredMethods
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|missing
init|=
name|methodHelper
operator|.
name|getMissingProperties
argument_list|(
name|endpoint
operator|.
name|getMethodName
argument_list|()
argument_list|,
name|argNames
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Missing properties for %s, need one or more from %s"
argument_list|,
name|endpoint
operator|.
name|getMethodName
argument_list|()
argument_list|,
name|missing
argument_list|)
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|filteredMethods
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// found an exact match
name|method
operator|=
name|filteredMethods
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|method
operator|=
name|methodHelper
operator|.
name|getHighestPriorityMethod
argument_list|(
name|filteredMethods
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"Calling highest priority operation {} from operations {}"
argument_list|,
name|method
argument_list|,
name|filteredMethods
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|method
return|;
block|}
end_function

begin_comment
comment|// returns false on exception, which is set in exchange
end_comment

begin_function
specifier|private
name|boolean
name|processInBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
specifier|final
name|String
name|inBodyProperty
init|=
name|endpoint
operator|.
name|getInBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|inBodyProperty
operator|!=
literal|null
condition|)
block|{
name|Object
name|value
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
try|try
block|{
name|value
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
name|inBodyProperty
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error converting value %s to property %s: %s"
argument_list|,
name|value
argument_list|,
name|inBodyProperty
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Property [{}] has message body value {}"
argument_list|,
name|inBodyProperty
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|inBodyProperty
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
end_function

begin_function
specifier|protected
specifier|static
specifier|synchronized
name|ExecutorService
name|getExecutorService
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
comment|// CamelContext will shutdown thread pool when it shutdown so we can
comment|// lazy create it on demand
comment|// but in case of hot-deploy or the likes we need to be able to
comment|// re-create it (its a shared static instance)
if|if
condition|(
name|executorService
operator|==
literal|null
operator|||
name|executorService
operator|.
name|isTerminated
argument_list|()
operator|||
name|executorService
operator|.
name|isShutdown
argument_list|()
condition|)
block|{
specifier|final
name|ExecutorServiceManager
name|manager
init|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
decl_stmt|;
comment|// try to lookup a pool first based on profile
name|ThreadPoolProfile
name|poolProfile
init|=
name|manager
operator|.
name|getThreadPoolProfile
argument_list|(
name|$
block|{
name|name
block|}
name|Constants
operator|.
name|THREAD_PROFILE_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|poolProfile
operator|==
literal|null
condition|)
block|{
name|poolProfile
operator|=
name|manager
operator|.
name|getDefaultThreadPoolProfile
argument_list|()
expr_stmt|;
block|}
comment|// create a new pool using the custom or default profile
name|executorService
operator|=
name|manager
operator|.
name|newScheduledThreadPool
argument_list|(
name|$
block|{
name|name
block|}
name|Producer
operator|.
name|class
argument_list|,
name|$
block|{
name|name
block|}
name|Constants
operator|.
name|THREAD_PROFILE_NAME
argument_list|,
name|poolProfile
argument_list|)
expr_stmt|;
block|}
return|return
name|executorService
return|;
block|}
end_function

unit|}
end_unit

