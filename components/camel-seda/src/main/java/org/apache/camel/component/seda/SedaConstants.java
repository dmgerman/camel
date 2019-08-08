begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
package|;
end_package

begin_class
DECL|class|SedaConstants
specifier|public
specifier|final
class|class
name|SedaConstants
block|{
DECL|field|MAX_CONCURRENT_CONSUMERS
specifier|public
specifier|static
specifier|final
name|int
name|MAX_CONCURRENT_CONSUMERS
init|=
literal|500
decl_stmt|;
DECL|field|CONCURRENT_CONSUMERS
specifier|public
specifier|static
specifier|final
name|int
name|CONCURRENT_CONSUMERS
init|=
literal|1
decl_stmt|;
DECL|field|QUEUE_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|QUEUE_SIZE
init|=
literal|1000
decl_stmt|;
DECL|method|SedaConstants ()
specifier|private
name|SedaConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

