begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AggregationStrategy
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
name|CamelContextAware
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
name|support
operator|.
name|service
operator|.
name|ServiceHelper
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
name|support
operator|.
name|service
operator|.
name|ServiceSupport
import|;
end_import

begin_comment
comment|/**  * An {@link AggregationStrategy} that adapts to a POJO.  *<p/>  * This allows end users to use POJOs for the aggregation logic, instead of having to implement the  * Camel API {@link AggregationStrategy}.  */
end_comment

begin_class
DECL|class|AggregationStrategyBeanAdapter
specifier|public
specifier|final
class|class
name|AggregationStrategyBeanAdapter
extends|extends
name|ServiceSupport
implements|implements
name|AggregationStrategy
implements|,
name|CamelContextAware
block|{
DECL|field|EXCLUDED_METHODS
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|Method
argument_list|>
name|EXCLUDED_METHODS
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|pojo
specifier|private
name|Object
name|pojo
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
DECL|field|allowNullOldExchange
specifier|private
name|boolean
name|allowNullOldExchange
decl_stmt|;
DECL|field|allowNullNewExchange
specifier|private
name|boolean
name|allowNullNewExchange
decl_stmt|;
DECL|field|mi
specifier|private
specifier|volatile
name|AggregationStrategyMethodInfo
name|mi
decl_stmt|;
static|static
block|{
comment|// exclude all java.lang.Object methods as we dont want to invoke them
name|EXCLUDED_METHODS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Object
operator|.
name|class
operator|.
name|getMethods
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// exclude all java.lang.reflect.Proxy methods as we dont want to invoke them
name|EXCLUDED_METHODS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Proxy
operator|.
name|class
operator|.
name|getMethods
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates this adapter.      *      * @param pojo the pojo to use.      */
DECL|method|AggregationStrategyBeanAdapter (Object pojo)
specifier|public
name|AggregationStrategyBeanAdapter
parameter_list|(
name|Object
name|pojo
parameter_list|)
block|{
name|this
argument_list|(
name|pojo
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates this adapter.      *      * @param type the class type of the pojo      */
DECL|method|AggregationStrategyBeanAdapter (Class<?> type)
specifier|public
name|AggregationStrategyBeanAdapter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|this
argument_list|(
name|type
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates this adapter.      *      * @param pojo the pojo to use.      * @param methodName the name of the method to call      */
DECL|method|AggregationStrategyBeanAdapter (Object pojo, String methodName)
specifier|public
name|AggregationStrategyBeanAdapter
parameter_list|(
name|Object
name|pojo
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|pojo
operator|=
name|pojo
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|pojo
operator|.
name|getClass
argument_list|()
expr_stmt|;
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
block|}
comment|/**      * Creates this adapter.      *      * @param type the class type of the pojo      * @param methodName the name of the method to call      */
DECL|method|AggregationStrategyBeanAdapter (Class<?> type, String methodName)
specifier|public
name|AggregationStrategyBeanAdapter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|pojo
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getMethodName ()
specifier|public
name|String
name|getMethodName
parameter_list|()
block|{
return|return
name|methodName
return|;
block|}
DECL|method|setMethodName (String methodName)
specifier|public
name|void
name|setMethodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
block|}
DECL|method|isAllowNullOldExchange ()
specifier|public
name|boolean
name|isAllowNullOldExchange
parameter_list|()
block|{
return|return
name|allowNullOldExchange
return|;
block|}
DECL|method|setAllowNullOldExchange (boolean allowNullOldExchange)
specifier|public
name|void
name|setAllowNullOldExchange
parameter_list|(
name|boolean
name|allowNullOldExchange
parameter_list|)
block|{
name|this
operator|.
name|allowNullOldExchange
operator|=
name|allowNullOldExchange
expr_stmt|;
block|}
DECL|method|isAllowNullNewExchange ()
specifier|public
name|boolean
name|isAllowNullNewExchange
parameter_list|()
block|{
return|return
name|allowNullNewExchange
return|;
block|}
DECL|method|setAllowNullNewExchange (boolean allowNullNewExchange)
specifier|public
name|void
name|setAllowNullNewExchange
parameter_list|(
name|boolean
name|allowNullNewExchange
parameter_list|)
block|{
name|this
operator|.
name|allowNullNewExchange
operator|=
name|allowNullNewExchange
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
if|if
condition|(
operator|!
name|allowNullOldExchange
operator|&&
name|oldExchange
operator|==
literal|null
condition|)
block|{
return|return
name|newExchange
return|;
block|}
if|if
condition|(
operator|!
name|allowNullNewExchange
operator|&&
name|newExchange
operator|==
literal|null
condition|)
block|{
return|return
name|oldExchange
return|;
block|}
try|try
block|{
name|Object
name|out
init|=
name|mi
operator|.
name|invoke
argument_list|(
name|pojo
argument_list|,
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|oldExchange
operator|!=
literal|null
condition|)
block|{
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|oldExchange
operator|!=
literal|null
condition|)
block|{
name|oldExchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newExchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|oldExchange
operator|!=
literal|null
condition|?
name|oldExchange
else|:
name|newExchange
return|;
block|}
comment|/**      * Validates whether the given method is valid.      *      * @param method  the method      * @return true if valid, false to skip the method      */
DECL|method|isValidMethod (Method method)
specifier|protected
name|boolean
name|isValidMethod
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
comment|// must not be in the excluded list
for|for
control|(
name|Method
name|excluded
range|:
name|EXCLUDED_METHODS
control|)
block|{
if|if
condition|(
name|method
operator|.
name|equals
argument_list|(
name|excluded
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|// must be a public method
if|if
condition|(
operator|!
name|Modifier
operator|.
name|isPublic
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// return type must not be void and it should not be a bridge method
if|if
condition|(
name|method
operator|.
name|getReturnType
argument_list|()
operator|.
name|equals
argument_list|(
name|Void
operator|.
name|TYPE
argument_list|)
operator|||
name|method
operator|.
name|isBridge
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|isStaticMethod (Method method)
specifier|private
specifier|static
name|boolean
name|isStaticMethod
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
return|return
name|Modifier
operator|.
name|isStatic
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|Method
name|found
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|methodName
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Method
name|method
range|:
name|type
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|isValidMethod
argument_list|(
name|method
argument_list|)
operator|&&
name|method
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|methodName
argument_list|)
condition|)
block|{
if|if
condition|(
name|found
operator|==
literal|null
condition|)
block|{
name|found
operator|=
name|method
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The bean "
operator|+
name|type
operator|+
literal|" has 2 or more methods with the name "
operator|+
name|methodName
argument_list|)
throw|;
block|}
block|}
block|}
block|}
else|else
block|{
for|for
control|(
name|Method
name|method
range|:
name|type
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|isValidMethod
argument_list|(
name|method
argument_list|)
condition|)
block|{
if|if
condition|(
name|found
operator|==
literal|null
condition|)
block|{
name|found
operator|=
name|method
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The bean "
operator|+
name|type
operator|+
literal|" has 2 or more methods and no explicit method name was configured."
argument_list|)
throw|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|found
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Cannot find a valid method with name: "
operator|+
name|methodName
operator|+
literal|" on bean type: "
operator|+
name|type
argument_list|)
throw|;
block|}
comment|// if its not a static method then we must have an instance of the pojo
if|if
condition|(
operator|!
name|isStaticMethod
argument_list|(
name|found
argument_list|)
operator|&&
name|pojo
operator|==
literal|null
condition|)
block|{
name|pojo
operator|=
name|camelContext
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
comment|// create the method info which has adapted to the pojo
name|AggregationStrategyBeanInfo
name|bi
init|=
operator|new
name|AggregationStrategyBeanInfo
argument_list|(
name|type
argument_list|,
name|found
argument_list|)
decl_stmt|;
name|mi
operator|=
name|bi
operator|.
name|createMethodInfo
argument_list|()
expr_stmt|;
comment|// in case the POJO is CamelContextAware
if|if
condition|(
name|pojo
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|pojo
operator|)
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// in case the pojo is a service
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|pojo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|pojo
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

