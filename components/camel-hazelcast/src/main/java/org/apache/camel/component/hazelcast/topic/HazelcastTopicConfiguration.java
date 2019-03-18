begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.topic
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|topic
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
import|;
end_import

begin_comment
comment|/**  * Hazelcast Topic Component configuration.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|HazelcastTopicConfiguration
specifier|public
class|class
name|HazelcastTopicConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|reliable
specifier|private
name|boolean
name|reliable
decl_stmt|;
comment|/**      * Define if the endpoint will use a reliable Topic struct or not.      */
DECL|method|isReliable ()
specifier|public
name|boolean
name|isReliable
parameter_list|()
block|{
return|return
name|reliable
return|;
block|}
DECL|method|setReliable (boolean reliable)
specifier|public
name|void
name|setReliable
parameter_list|(
name|boolean
name|reliable
parameter_list|)
block|{
name|this
operator|.
name|reliable
operator|=
name|reliable
expr_stmt|;
block|}
block|}
end_class

end_unit

