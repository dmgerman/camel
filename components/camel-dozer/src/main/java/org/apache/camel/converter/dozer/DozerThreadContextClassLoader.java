begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.dozer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|dozer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dozermapper
operator|.
name|core
operator|.
name|util
operator|.
name|DozerClassLoader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dozermapper
operator|.
name|core
operator|.
name|util
operator|.
name|MappingUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang3
operator|.
name|ClassUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang3
operator|.
name|StringUtils
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

begin_class
DECL|class|DozerThreadContextClassLoader
specifier|public
class|class
name|DozerThreadContextClassLoader
implements|implements
name|DozerClassLoader
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DozerThreadContextClassLoader
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|loadClass (String className)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loading class from classloader: {}."
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// try to resolve the class from the thread context classloader
name|result
operator|=
name|ClassUtils
operator|.
name|getClass
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|,
name|className
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|MappingUtils
operator|.
name|throwMappingException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|loadResource (String uri)
specifier|public
name|URL
name|loadResource
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|URL
name|answer
init|=
literal|null
decl_stmt|;
name|ClassLoader
name|cl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|cl
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loading resource from classloader: {}."
argument_list|,
name|cl
argument_list|)
expr_stmt|;
name|answer
operator|=
name|cl
operator|.
name|getResource
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
comment|// try treating it as a system resource
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|ClassLoader
operator|.
name|getSystemResource
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
comment|// one more time in case it's a normal URI
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|StringUtils
operator|.
name|contains
argument_list|(
name|uri
argument_list|,
literal|":"
argument_list|)
condition|)
block|{
try|try
block|{
name|answer
operator|=
operator|new
name|URL
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|MappingUtils
operator|.
name|throwMappingException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

