begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
package|;
end_package

begin_comment
comment|/**  * A list of possible backpressure strategy to use when the emission of upstream items cannot respect backpressure.  */
end_comment

begin_enum
DECL|enum|ReactiveStreamsBackpressureStrategy
specifier|public
enum|enum
name|ReactiveStreamsBackpressureStrategy
block|{
comment|/**      * Buffers<em>all</em> onNext values until the downstream consumes it.      */
DECL|enumConstant|BUFFER
name|BUFFER
block|,
comment|/**      * Drops the most recent onNext value if the downstream can't keep up.      */
DECL|enumConstant|DROP
name|DROP
block|,
comment|/**      * Keeps only the latest onNext value, overwriting any previous value if the      * downstream can't keep up.      */
DECL|enumConstant|LATEST
name|LATEST
block|}
end_enum

end_unit

