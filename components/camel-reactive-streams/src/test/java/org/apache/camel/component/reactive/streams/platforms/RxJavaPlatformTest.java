begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.platforms
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
operator|.
name|platforms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|io
operator|.
name|reactivex
operator|.
name|Flowable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Publisher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Subscriber
import|;
end_import

begin_class
DECL|class|RxJavaPlatformTest
specifier|public
class|class
name|RxJavaPlatformTest
extends|extends
name|AbstractPlatformTestSupport
block|{
annotation|@
name|Override
DECL|method|changeSign (Publisher<Integer> data, Consumer<Integer> consume)
specifier|protected
name|void
name|changeSign
parameter_list|(
name|Publisher
argument_list|<
name|Integer
argument_list|>
name|data
parameter_list|,
name|Consumer
argument_list|<
name|Integer
argument_list|>
name|consume
parameter_list|)
block|{
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|data
argument_list|)
operator|.
name|map
argument_list|(
name|i
lambda|->
operator|-
name|i
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|consume
operator|::
name|accept
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|changeSign (Iterable<Integer> data, Subscriber<Integer> camel)
specifier|protected
name|void
name|changeSign
parameter_list|(
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|data
parameter_list|,
name|Subscriber
argument_list|<
name|Integer
argument_list|>
name|camel
parameter_list|)
block|{
name|Flowable
operator|.
name|fromIterable
argument_list|(
name|data
argument_list|)
operator|.
name|map
argument_list|(
name|i
lambda|->
operator|-
name|i
argument_list|)
operator|.
name|subscribe
argument_list|(
name|camel
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

