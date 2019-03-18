begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.xray.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|component
package|;
end_package

begin_class
DECL|class|CommonEndpoints
specifier|public
specifier|final
class|class
name|CommonEndpoints
block|{
DECL|field|RECEIVED
specifier|public
specifier|static
specifier|final
name|String
name|RECEIVED
init|=
literal|"xray-test:received"
decl_stmt|;
DECL|field|PROCESSING
specifier|public
specifier|static
specifier|final
name|String
name|PROCESSING
init|=
literal|"xray-test:processing"
decl_stmt|;
DECL|field|PERSISTENCE_QUEUE
specifier|public
specifier|static
specifier|final
name|String
name|PERSISTENCE_QUEUE
init|=
literal|"xray-test:persistence-queue"
decl_stmt|;
DECL|field|READY
specifier|public
specifier|static
specifier|final
name|String
name|READY
init|=
literal|"xray-test:ready"
decl_stmt|;
DECL|method|CommonEndpoints ()
specifier|private
name|CommonEndpoints
parameter_list|()
block|{      }
block|}
end_class

end_unit

