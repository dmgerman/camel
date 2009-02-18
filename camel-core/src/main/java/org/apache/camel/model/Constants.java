begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|Constants
specifier|public
class|class
name|Constants
block|{
DECL|field|JAXB_CONTEXT_PACKAGES
specifier|public
specifier|static
specifier|final
name|String
name|JAXB_CONTEXT_PACKAGES
init|=
literal|"org.apache.camel:org.apache.camel.model:org.apache.camel.model.config:org.apache.camel.model.dataformat:org.apache.camel.model.language:org.apache.camel.model.loadbalancer"
decl_stmt|;
block|}
end_class

end_unit

