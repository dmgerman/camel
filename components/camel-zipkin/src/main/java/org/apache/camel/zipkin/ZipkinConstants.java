begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.zipkin
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|zipkin
package|;
end_package

begin_comment
comment|/**  * Contains the header keys that are used to represent trace id, span id, parent span id, sampled.  *<p/>  * The names correspond with the zipkin header values.  */
end_comment

begin_class
DECL|class|ZipkinConstants
specifier|public
specifier|final
class|class
name|ZipkinConstants
block|{
DECL|field|TRACE_ID
specifier|public
specifier|static
specifier|final
name|String
name|TRACE_ID
init|=
literal|"X-B3-TraceId"
decl_stmt|;
DECL|field|SPAN_ID
specifier|public
specifier|static
specifier|final
name|String
name|SPAN_ID
init|=
literal|"X-B3-SpanId"
decl_stmt|;
DECL|field|PARENT_SPAN_ID
specifier|public
specifier|static
specifier|final
name|String
name|PARENT_SPAN_ID
init|=
literal|"X-B3-ParentSpanId"
decl_stmt|;
DECL|field|SAMPLED
specifier|public
specifier|static
specifier|final
name|String
name|SAMPLED
init|=
literal|"X-B3-Sampled"
decl_stmt|;
DECL|method|ZipkinConstants ()
specifier|private
name|ZipkinConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

