begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|ClassResolver
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
name|CastUtils
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

begin_comment
comment|/**  * Default class resolver that uses regular class loader to load classes.  */
end_comment

begin_class
DECL|class|DefaultClassResolver
specifier|public
class|class
name|DefaultClassResolver
implements|implements
name|ClassResolver
block|{
DECL|method|resolveClass (String name)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|resolveClass
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|loadClass
argument_list|(
name|name
argument_list|,
name|DefaultClassResolver
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
return|;
block|}
DECL|method|resolveClass (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Class
argument_list|<
name|T
argument_list|>
name|resolveClass
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Class
argument_list|<
name|T
argument_list|>
name|answer
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|loadClass
argument_list|(
name|name
argument_list|,
name|DefaultClassResolver
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|(
name|Class
argument_list|<
name|T
argument_list|>
operator|)
name|answer
return|;
block|}
DECL|method|resolveClass (String name, ClassLoader loader)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|resolveClass
parameter_list|(
name|String
name|name
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
block|{
return|return
name|loadClass
argument_list|(
name|name
argument_list|,
name|loader
argument_list|)
return|;
block|}
DECL|method|resolveClass (String name, Class<T> type, ClassLoader loader)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Class
argument_list|<
name|T
argument_list|>
name|resolveClass
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
block|{
name|Class
argument_list|<
name|T
argument_list|>
name|answer
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|loadClass
argument_list|(
name|name
argument_list|,
name|loader
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|resolveMandatoryClass (String name)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|resolveMandatoryClass
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|answer
init|=
name|resolveClass
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
name|name
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|resolveMandatoryClass (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Class
argument_list|<
name|T
argument_list|>
name|resolveMandatoryClass
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|Class
argument_list|<
name|T
argument_list|>
name|answer
init|=
name|resolveClass
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
name|name
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|resolveMandatoryClass (String name, ClassLoader loader)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|resolveMandatoryClass
parameter_list|(
name|String
name|name
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|answer
init|=
name|resolveClass
argument_list|(
name|name
argument_list|,
name|loader
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
name|name
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|resolveMandatoryClass (String name, Class<T> type, ClassLoader loader)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Class
argument_list|<
name|T
argument_list|>
name|resolveMandatoryClass
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|Class
argument_list|<
name|T
argument_list|>
name|answer
init|=
name|resolveClass
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|loader
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
name|name
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|loadResourceAsStream (String uri)
specifier|public
name|InputStream
name|loadResourceAsStream
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|uri
argument_list|,
literal|"uri"
argument_list|)
expr_stmt|;
return|return
name|ObjectHelper
operator|.
name|loadResourceAsStream
argument_list|(
name|uri
argument_list|)
return|;
block|}
DECL|method|loadResourceAsURL (String uri)
specifier|public
name|URL
name|loadResourceAsURL
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|uri
argument_list|,
literal|"uri"
argument_list|)
expr_stmt|;
return|return
name|ObjectHelper
operator|.
name|loadResourceAsURL
argument_list|(
name|uri
argument_list|)
return|;
block|}
DECL|method|loadClass (String name, ClassLoader loader)
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass
parameter_list|(
name|String
name|name
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|name
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
return|return
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|name
argument_list|,
name|loader
argument_list|)
return|;
block|}
block|}
end_class

end_unit

