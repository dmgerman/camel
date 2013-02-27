begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

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
name|impl
operator|.
name|DefaultComponent
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
name|ProcessorEndpoint
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
name|LRUSoftCache
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

begin_comment
comment|/**  * The<a href="http://camel.apache.org/bean.html">Bean Component</a>  * will look up the URI in the {@link org.apache.camel.spi.Registry} and use that to handle message dispatching.  *  * @version   */
end_comment

begin_class
DECL|class|BeanComponent
specifier|public
class|class
name|BeanComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BeanComponent
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// use an internal soft cache for BeanInfo as they are costly to introspect
comment|// for example the bean language using OGNL expression runs much faster reusing the BeanInfo from this cache
DECL|field|cache
specifier|private
specifier|final
name|LRUSoftCache
argument_list|<
name|BeanInfoCacheKey
argument_list|,
name|BeanInfo
argument_list|>
name|cache
init|=
operator|new
name|LRUSoftCache
argument_list|<
name|BeanInfoCacheKey
argument_list|,
name|BeanInfo
argument_list|>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
DECL|method|BeanComponent ()
specifier|public
name|BeanComponent
parameter_list|()
block|{     }
comment|/**      * A helper method to create a new endpoint from a bean with a generated URI      */
DECL|method|createEndpoint (Object bean)
specifier|public
name|ProcessorEndpoint
name|createEndpoint
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
comment|// used by servicemix-camel
name|String
name|uri
init|=
literal|"bean:generated:"
operator|+
name|bean
decl_stmt|;
return|return
name|createEndpoint
argument_list|(
name|bean
argument_list|,
name|uri
argument_list|)
return|;
block|}
comment|/**      * A helper method to create a new endpoint from a bean with a given URI      */
DECL|method|createEndpoint (Object bean, String uri)
specifier|public
name|ProcessorEndpoint
name|createEndpoint
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
comment|// used by servicemix-camel
name|BeanProcessor
name|processor
init|=
operator|new
name|BeanProcessor
argument_list|(
name|bean
argument_list|,
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|processor
argument_list|)
return|;
block|}
comment|// Implementation methods
comment|//-----------------------------------------------------------------------
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|BeanEndpoint
name|endpoint
init|=
operator|new
name|BeanEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setBeanName
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|Boolean
name|cache
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"cache"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|endpoint
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|processor
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|createEndpoint (String uri, BeanProcessor processor)
specifier|protected
name|BeanEndpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|BeanProcessor
name|processor
parameter_list|)
block|{
return|return
operator|new
name|BeanEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|getBeanInfoFromCache (BeanInfoCacheKey key)
name|BeanInfo
name|getBeanInfoFromCache
parameter_list|(
name|BeanInfoCacheKey
name|key
parameter_list|)
block|{
return|return
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|addBeanInfoToCache (BeanInfoCacheKey key, BeanInfo beanInfo)
name|void
name|addBeanInfoToCache
parameter_list|(
name|BeanInfoCacheKey
name|key
parameter_list|,
name|BeanInfo
name|beanInfo
parameter_list|)
block|{
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|beanInfo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
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
literal|"Clearing BeanInfo cache[size={}, hits={}, misses={}, evicted={}]"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|cache
operator|.
name|size
argument_list|()
block|,
name|cache
operator|.
name|getHits
argument_list|()
block|,
name|cache
operator|.
name|getMisses
argument_list|()
block|,
name|cache
operator|.
name|getEvicted
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

