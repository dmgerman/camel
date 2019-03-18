begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.ognl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|ognl
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
name|ognl
operator|.
name|ClassResolver
import|;
end_import

begin_comment
comment|/**  * This class is used to wrap the org.apache.camel.spi.ClassResolver with ClassResolver interface  */
end_comment

begin_class
DECL|class|CamelClassResolver
specifier|public
class|class
name|CamelClassResolver
implements|implements
name|ClassResolver
block|{
DECL|field|delegateClassResolver
specifier|private
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|ClassResolver
name|delegateClassResolver
decl_stmt|;
DECL|method|CamelClassResolver (org.apache.camel.spi.ClassResolver resolver)
specifier|public
name|CamelClassResolver
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|ClassResolver
name|resolver
parameter_list|)
block|{
name|delegateClassResolver
operator|=
name|resolver
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|classForName (String className, Map context)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|classForName
parameter_list|(
name|String
name|className
parameter_list|,
name|Map
name|context
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
return|return
name|delegateClassResolver
operator|.
name|resolveClass
argument_list|(
name|className
argument_list|)
return|;
block|}
block|}
end_class

end_unit

