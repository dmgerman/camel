begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.xml.util.jsse
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|xml
operator|.
name|util
operator|.
name|jsse
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
name|core
operator|.
name|xml
operator|.
name|AbstractCamelFactoryBean
import|;
end_import

begin_class
DECL|class|AbstractJsseUtilFactoryBean
specifier|public
specifier|abstract
class|class
name|AbstractJsseUtilFactoryBean
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractCamelFactoryBean
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|Override
DECL|method|getObject ()
specifier|public
specifier|abstract
name|T
name|getObject
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getObjectType ()
specifier|public
specifier|abstract
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|getObjectType
parameter_list|()
function_decl|;
block|}
end_class

end_unit

