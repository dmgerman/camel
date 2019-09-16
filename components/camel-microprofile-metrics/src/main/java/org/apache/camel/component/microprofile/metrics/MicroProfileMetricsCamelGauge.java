begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.microprofile.metrics
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
package|;
end_package

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|Gauge
import|;
end_import

begin_class
DECL|class|MicroProfileMetricsCamelGauge
class|class
name|MicroProfileMetricsCamelGauge
implements|implements
name|Gauge
argument_list|<
name|Number
argument_list|>
block|{
DECL|field|value
specifier|private
name|Number
name|value
init|=
literal|0
decl_stmt|;
annotation|@
name|Override
DECL|method|getValue ()
specifier|public
name|Number
name|getValue
parameter_list|()
block|{
return|return
name|this
operator|.
name|value
return|;
block|}
DECL|method|setValue (Number value)
specifier|public
name|void
name|setValue
parameter_list|(
name|Number
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

