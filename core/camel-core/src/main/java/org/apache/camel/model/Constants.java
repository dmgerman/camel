begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Various constants.  */
end_comment

begin_class
DECL|class|Constants
specifier|public
specifier|final
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
literal|""
operator|+
literal|"org.apache.camel:"
operator|+
literal|"org.apache.camel.model:"
operator|+
literal|"org.apache.camel.model.cloud:"
operator|+
literal|"org.apache.camel.model.config:"
operator|+
literal|"org.apache.camel.model.dataformat:"
operator|+
literal|"org.apache.camel.model.language:"
operator|+
literal|"org.apache.camel.model.loadbalancer:"
operator|+
literal|"org.apache.camel.model.rest:"
operator|+
literal|"org.apache.camel.model.transformer:"
operator|+
literal|"org.apache.camel.model.validator"
decl_stmt|;
DECL|field|PLACEHOLDER_QNAME
specifier|public
specifier|static
specifier|final
name|String
name|PLACEHOLDER_QNAME
init|=
literal|"http://camel.apache.org/schema/placeholder"
decl_stmt|;
DECL|method|Constants ()
specifier|private
name|Constants
parameter_list|()
block|{     }
block|}
end_class

end_unit

