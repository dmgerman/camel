begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|osgi
package|;
end_package

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
name|Component
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
name|component
operator|.
name|file
operator|.
name|FileComponent
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
name|core
operator|.
name|osgi
operator|.
name|test
operator|.
name|MyService
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
name|osgi
operator|.
name|Activator
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
name|language
operator|.
name|simple
operator|.
name|SimpleLanguage
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
name|ComponentResolver
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
name|Language
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
name|LanguageResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|mock
operator|.
name|MockBundleContext
import|;
end_import

begin_class
DECL|class|CamelMockBundleContext
specifier|public
class|class
name|CamelMockBundleContext
extends|extends
name|MockBundleContext
block|{
DECL|method|CamelMockBundleContext (Bundle bundle)
specifier|public
name|CamelMockBundleContext
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|super
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
DECL|method|getService (ServiceReference reference)
specifier|public
name|Object
name|getService
parameter_list|(
name|ServiceReference
name|reference
parameter_list|)
block|{
name|String
index|[]
name|classNames
init|=
operator|(
name|String
index|[]
operator|)
name|reference
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|OBJECTCLASS
argument_list|)
decl_stmt|;
if|if
condition|(
name|classNames
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
literal|"org.apache.camel.osgi.test.MyService"
argument_list|)
condition|)
block|{
return|return
operator|new
name|MyService
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|classNames
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|ComponentResolver
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|ComponentResolver
argument_list|()
block|{
specifier|public
name|Component
name|resolveComponent
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
literal|"file_test"
argument_list|)
condition|)
block|{
return|return
operator|new
name|FileComponent
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
return|;
block|}
elseif|else
if|if
condition|(
name|classNames
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|LanguageResolver
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|LanguageResolver
argument_list|()
block|{
specifier|public
name|Language
name|resolveLanguage
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
literal|"simple"
argument_list|)
condition|)
block|{
return|return
operator|new
name|SimpleLanguage
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

