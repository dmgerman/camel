begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cmis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cmis
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
name|chemistry
operator|.
name|opencmis
operator|.
name|client
operator|.
name|api
operator|.
name|SessionFactory
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
DECL|class|SessionFactoryLocator
specifier|public
specifier|final
class|class
name|SessionFactoryLocator
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
name|SessionFactoryLocator
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|sessionFactory
specifier|private
specifier|static
name|SessionFactory
name|sessionFactory
decl_stmt|;
DECL|method|SessionFactoryLocator ()
specifier|private
name|SessionFactoryLocator
parameter_list|()
block|{
comment|//Utils class
block|}
DECL|method|setSessionFactory (SessionFactory factory)
specifier|public
specifier|static
name|void
name|setSessionFactory
parameter_list|(
name|SessionFactory
name|factory
parameter_list|)
block|{
name|sessionFactory
operator|=
name|factory
expr_stmt|;
block|}
DECL|method|getSessionFactory ()
specifier|public
specifier|static
name|SessionFactory
name|getSessionFactory
parameter_list|()
block|{
if|if
condition|(
name|sessionFactory
operator|!=
literal|null
condition|)
block|{
return|return
name|sessionFactory
return|;
block|}
else|else
block|{
comment|// create the sessionFactory in another way
name|sessionFactory
operator|=
name|loadSessionFactoryFromClassPath
argument_list|()
expr_stmt|;
return|return
name|sessionFactory
return|;
block|}
block|}
DECL|method|loadSessionFactoryFromClassPath ()
specifier|private
specifier|static
name|SessionFactory
name|loadSessionFactoryFromClassPath
parameter_list|()
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|factoryClass
init|=
literal|null
decl_stmt|;
name|factoryClass
operator|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl"
argument_list|)
expr_stmt|;
if|if
condition|(
name|factoryClass
operator|!=
literal|null
condition|)
block|{
name|Method
name|method
init|=
name|factoryClass
operator|.
name|getMethod
argument_list|(
literal|"newInstance"
argument_list|,
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
return|return
operator|(
name|SessionFactory
operator|)
name|method
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Cannot create the SessionFactoryImpl due to: {0}"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

