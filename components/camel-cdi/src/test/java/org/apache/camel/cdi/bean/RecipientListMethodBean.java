begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
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
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|ApplicationScoped
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
name|Consume
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
name|RecipientList
import|;
end_import

begin_class
annotation|@
name|ApplicationScoped
DECL|class|RecipientListMethodBean
specifier|public
class|class
name|RecipientListMethodBean
block|{
annotation|@
name|RecipientList
annotation|@
name|Consume
argument_list|(
literal|"direct:inbound"
argument_list|)
DECL|method|route ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|route
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
literal|"mock:outbound1"
argument_list|,
literal|"mock:outbound2"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

