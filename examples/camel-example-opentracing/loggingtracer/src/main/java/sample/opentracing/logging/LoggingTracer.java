begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|sample.opentracing.logging
package|package
name|sample
operator|.
name|opentracing
operator|.
name|logging
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|mock
operator|.
name|MockSpan
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|mock
operator|.
name|MockTracer
import|;
end_import

begin_class
DECL|class|LoggingTracer
specifier|public
class|class
name|LoggingTracer
extends|extends
name|MockTracer
block|{
DECL|method|LoggingTracer ()
specifier|public
name|LoggingTracer
parameter_list|()
block|{
name|super
argument_list|(
name|MockTracer
operator|.
name|Propagator
operator|.
name|TEXT_MAP
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onSpanFinished (MockSpan mockSpan)
specifier|protected
name|void
name|onSpanFinished
parameter_list|(
name|MockSpan
name|mockSpan
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"SPAN FINISHED: traceId="
operator|+
name|mockSpan
operator|.
name|context
argument_list|()
operator|.
name|traceId
argument_list|()
operator|+
literal|" spanId="
operator|+
name|mockSpan
operator|.
name|context
argument_list|()
operator|.
name|spanId
argument_list|()
operator|+
literal|" parentId="
operator|+
name|mockSpan
operator|.
name|parentId
argument_list|()
operator|+
literal|" operation="
operator|+
name|mockSpan
operator|.
name|operationName
argument_list|()
operator|+
literal|" tags="
operator|+
name|mockSpan
operator|.
name|tags
argument_list|()
operator|+
literal|" logs=["
operator|+
name|toText
argument_list|(
name|mockSpan
operator|.
name|logEntries
argument_list|()
argument_list|)
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
DECL|method|toText (List<MockSpan.LogEntry> logEntries)
specifier|protected
name|String
name|toText
parameter_list|(
name|List
argument_list|<
name|MockSpan
operator|.
name|LogEntry
argument_list|>
name|logEntries
parameter_list|)
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|logEntries
operator|.
name|forEach
argument_list|(
name|entry
lambda|->
name|builder
operator|.
name|append
argument_list|(
name|entry
operator|.
name|fields
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

