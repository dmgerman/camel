begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.chatscript.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|chatscript
operator|.
name|utils
package|;
end_package

begin_class
DECL|class|ChatScriptConstants
specifier|public
specifier|final
class|class
name|ChatScriptConstants
block|{
DECL|field|DEFAULT_PORT
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_PORT
init|=
literal|1024
decl_stmt|;
DECL|field|URI_ERROR
specifier|public
specifier|static
specifier|final
name|String
name|URI_ERROR
init|=
literal|"Invalid URI. Format must be of the form chatscript://host[:port]/botname?[options...]"
decl_stmt|;
DECL|method|ChatScriptConstants ()
specifier|private
name|ChatScriptConstants
parameter_list|()
block|{
comment|// restrict instantiation
block|}
block|}
end_class

end_unit

