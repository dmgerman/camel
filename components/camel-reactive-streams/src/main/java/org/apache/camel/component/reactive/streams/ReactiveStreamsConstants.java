begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Useful constants used in the Camel Reactive Streams component.  */
end_comment

begin_class
DECL|class|ReactiveStreamsConstants
specifier|public
specifier|final
class|class
name|ReactiveStreamsConstants
block|{
DECL|field|SCHEME
specifier|public
specifier|static
specifier|final
name|String
name|SCHEME
init|=
literal|"reactive-streams"
decl_stmt|;
DECL|field|SERVICE_PATH
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_PATH
init|=
literal|"META-INF/services/org/apache/camel/reactive-streams/"
decl_stmt|;
DECL|field|DEFAULT_SERVICE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_SERVICE_NAME
init|=
literal|"default-service"
decl_stmt|;
comment|/**      * Every exchange consumed by Camel has this header set to indicate if the exchange      * contains an item (value="onNext"), an error (value="onError") or a completion event (value="onComplete").      * Errors and completion notification are not forwarded by default.      */
DECL|field|REACTIVE_STREAMS_EVENT_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|REACTIVE_STREAMS_EVENT_TYPE
init|=
literal|"CamelReactiveStreamsEventType"
decl_stmt|;
DECL|field|REACTIVE_STREAMS_CALLBACK
specifier|public
specifier|static
specifier|final
name|String
name|REACTIVE_STREAMS_CALLBACK
init|=
literal|"CamelReactiveStreamsCallback"
decl_stmt|;
DECL|method|ReactiveStreamsConstants ()
specifier|private
name|ReactiveStreamsConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

