begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven.packaging.srcgen
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|packaging
operator|.
name|srcgen
package|;
end_package

begin_class
DECL|class|Param
specifier|public
class|class
name|Param
block|{
DECL|field|type
name|GenericType
name|type
decl_stmt|;
DECL|field|name
name|String
name|name
decl_stmt|;
DECL|method|Param (GenericType type, String name)
specifier|public
name|Param
parameter_list|(
name|GenericType
name|type
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
block|}
end_class

end_unit

