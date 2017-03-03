begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.box2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|box2
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
name|component
operator|.
name|box2
operator|.
name|internal
operator|.
name|Box2ApiName
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
name|box2
operator|.
name|internal
operator|.
name|Box2PropertiesHelper
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
name|util
operator|.
name|component
operator|.
name|AbstractApiProducer
import|;
end_import

begin_comment
comment|/**  * The Box2 producer.  *   * @author<a href="mailto:punkhornsw@gmail.com">William Collins</a>  *   */
end_comment

begin_class
DECL|class|Box2Producer
specifier|public
class|class
name|Box2Producer
extends|extends
name|AbstractApiProducer
argument_list|<
name|Box2ApiName
argument_list|,
name|Box2Configuration
argument_list|>
block|{
DECL|method|Box2Producer (Box2Endpoint endpoint)
specifier|public
name|Box2Producer
parameter_list|(
name|Box2Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|Box2PropertiesHelper
operator|.
name|getHelper
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

