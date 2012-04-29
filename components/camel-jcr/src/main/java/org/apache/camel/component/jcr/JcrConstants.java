begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcr
package|;
end_package

begin_comment
comment|/**  * JCR Constants.  */
end_comment

begin_class
DECL|class|JcrConstants
specifier|public
specifier|final
class|class
name|JcrConstants
block|{
comment|/**      * Property key for specifying the name of a node in the repository      */
DECL|field|JCR_NODE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|JCR_NODE_NAME
init|=
literal|"CamelJcrNodeName"
decl_stmt|;
DECL|field|JCR_OPERATION
specifier|public
specifier|static
specifier|final
name|String
name|JCR_OPERATION
init|=
literal|"CamelJcrOperation"
decl_stmt|;
DECL|field|JCR_INSERT
specifier|public
specifier|static
specifier|final
name|String
name|JCR_INSERT
init|=
literal|"CamelJcrInsert"
decl_stmt|;
DECL|field|JCR_GET_BY_ID
specifier|public
specifier|static
specifier|final
name|String
name|JCR_GET_BY_ID
init|=
literal|"CamelJcrGetById"
decl_stmt|;
DECL|method|JcrConstants ()
specifier|private
name|JcrConstants
parameter_list|()
block|{
comment|// Utility class
block|}
block|}
end_class

end_unit

