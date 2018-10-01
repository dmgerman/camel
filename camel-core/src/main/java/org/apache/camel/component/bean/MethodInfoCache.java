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
name|util
operator|.
name|LRUCacheFactory
import|;
end_import

begin_comment
comment|/**  * Represents a cache of {@link MethodInfo} objects to avoid the expense of introspection for each  * invocation of a method via a proxy.  */
end_comment

begin_class
DECL|class|MethodInfoCache
specifier|public
class|class
name|MethodInfoCache
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|methodCache
specifier|private
name|Map
argument_list|<
name|Method
argument_list|,
name|MethodInfo
argument_list|>
name|methodCache
decl_stmt|;
DECL|field|classCache
specifier|private
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|BeanInfo
argument_list|>
name|classCache
decl_stmt|;
DECL|method|MethodInfoCache (CamelContext camelContext)
specifier|public
name|MethodInfoCache
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
argument_list|(
name|camelContext
argument_list|,
literal|1000
argument_list|,
literal|10000
argument_list|)
expr_stmt|;
block|}
DECL|method|MethodInfoCache (CamelContext camelContext, int classCacheSize, int methodCacheSize)
specifier|public
name|MethodInfoCache
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|int
name|classCacheSize
parameter_list|,
name|int
name|methodCacheSize
parameter_list|)
block|{
name|this
argument_list|(
name|camelContext
argument_list|,
name|createClassCache
argument_list|(
name|classCacheSize
argument_list|)
argument_list|,
name|createMethodCache
argument_list|(
name|methodCacheSize
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|MethodInfoCache (CamelContext camelContext, Map<Class<?>, BeanInfo> classCache, Map<Method, MethodInfo> methodCache)
specifier|public
name|MethodInfoCache
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|BeanInfo
argument_list|>
name|classCache
parameter_list|,
name|Map
argument_list|<
name|Method
argument_list|,
name|MethodInfo
argument_list|>
name|methodCache
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|classCache
operator|=
name|classCache
expr_stmt|;
name|this
operator|.
name|methodCache
operator|=
name|methodCache
expr_stmt|;
block|}
DECL|method|getMethodInfo (Method method)
specifier|public
specifier|synchronized
name|MethodInfo
name|getMethodInfo
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|MethodInfo
name|answer
init|=
name|methodCache
operator|.
name|get
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|createMethodInfo
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|methodCache
operator|.
name|put
argument_list|(
name|method
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|createMethodInfo (Method method)
specifier|protected
name|MethodInfo
name|createMethodInfo
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|declaringClass
init|=
name|method
operator|.
name|getDeclaringClass
argument_list|()
decl_stmt|;
name|BeanInfo
name|info
init|=
name|getBeanInfo
argument_list|(
name|declaringClass
argument_list|)
decl_stmt|;
return|return
name|info
operator|.
name|getMethodInfo
argument_list|(
name|method
argument_list|)
return|;
block|}
DECL|method|getBeanInfo (Class<?> declaringClass)
specifier|protected
specifier|synchronized
name|BeanInfo
name|getBeanInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|declaringClass
parameter_list|)
block|{
name|BeanInfo
name|beanInfo
init|=
name|classCache
operator|.
name|get
argument_list|(
name|declaringClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|beanInfo
operator|==
literal|null
condition|)
block|{
name|beanInfo
operator|=
name|createBeanInfo
argument_list|(
name|declaringClass
argument_list|)
expr_stmt|;
name|classCache
operator|.
name|put
argument_list|(
name|declaringClass
argument_list|,
name|beanInfo
argument_list|)
expr_stmt|;
block|}
return|return
name|beanInfo
return|;
block|}
DECL|method|createBeanInfo (Class<?> declaringClass)
specifier|protected
name|BeanInfo
name|createBeanInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|declaringClass
parameter_list|)
block|{
return|return
operator|new
name|BeanInfo
argument_list|(
name|camelContext
argument_list|,
name|declaringClass
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createLruCache (int size)
specifier|protected
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createLruCache
parameter_list|(
name|int
name|size
parameter_list|)
block|{
comment|// use a soft cache
return|return
name|LRUCacheFactory
operator|.
name|newLRUSoftCache
argument_list|(
name|size
argument_list|)
return|;
block|}
DECL|method|createClassCache (int size)
specifier|private
specifier|static
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|BeanInfo
argument_list|>
name|createClassCache
parameter_list|(
name|int
name|size
parameter_list|)
block|{
return|return
name|createLruCache
argument_list|(
name|size
argument_list|)
return|;
block|}
DECL|method|createMethodCache (int size)
specifier|private
specifier|static
name|Map
argument_list|<
name|Method
argument_list|,
name|MethodInfo
argument_list|>
name|createMethodCache
parameter_list|(
name|int
name|size
parameter_list|)
block|{
return|return
name|createLruCache
argument_list|(
name|size
argument_list|)
return|;
block|}
block|}
end_class

end_unit

