begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.jdbc
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
operator|.
name|jdbc
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectStreamClass
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
import|;
end_import

begin_comment
comment|/**  * This class is copied from the Apache ActiveMQ project.  */
end_comment

begin_class
DECL|class|ClassLoadingAwareObjectInputStream
specifier|public
class|class
name|ClassLoadingAwareObjectInputStream
extends|extends
name|ObjectInputStream
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|ClassLoadingAwareObjectInputStream (CamelContext camelContext, InputStream in)
specifier|public
name|ClassLoadingAwareObjectInputStream
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resolveClass (ObjectStreamClass classDesc)
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|resolveClass
parameter_list|(
name|ObjectStreamClass
name|classDesc
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
return|return
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|classDesc
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|resolveProxyClass (String[] interfaces)
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|resolveProxyClass
parameter_list|(
name|String
index|[]
name|interfaces
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|cinterfaces
init|=
operator|new
name|Class
index|[
name|interfaces
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|interfaces
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|cinterfaces
index|[
name|i
index|]
operator|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|interfaces
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
try|try
block|{
return|return
name|Proxy
operator|.
name|getProxyClass
argument_list|(
name|cinterfaces
index|[
literal|0
index|]
operator|.
name|getClassLoader
argument_list|()
argument_list|,
name|cinterfaces
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
literal|null
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

