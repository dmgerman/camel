begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.mvel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|mvel
package|;
end_package

begin_comment
comment|/**  * Mvel constants  */
end_comment

begin_class
DECL|class|MvelConstants
specifier|public
specifier|final
class|class
name|MvelConstants
block|{
DECL|field|MVEL_RESOURCE_URI
specifier|public
specifier|static
specifier|final
name|String
name|MVEL_RESOURCE_URI
init|=
literal|"CamelMvelResourceUri"
decl_stmt|;
DECL|field|MVEL_TEMPLATE
specifier|public
specifier|static
specifier|final
name|String
name|MVEL_TEMPLATE
init|=
literal|"CamelMvelTemplate"
decl_stmt|;
DECL|method|MvelConstants ()
specifier|private
name|MvelConstants
parameter_list|()
block|{
comment|// Utility class
block|}
block|}
end_class

end_unit

