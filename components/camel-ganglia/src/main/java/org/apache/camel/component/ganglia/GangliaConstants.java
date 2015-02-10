begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ganglia
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
package|;
end_package

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|GangliaConstants
specifier|public
specifier|final
class|class
name|GangliaConstants
block|{
DECL|field|GROUP_NAME
specifier|public
specifier|static
specifier|final
name|String
name|GROUP_NAME
init|=
literal|"CamelGangliaGroupName"
decl_stmt|;
DECL|field|METRIC_NAME
specifier|public
specifier|static
specifier|final
name|String
name|METRIC_NAME
init|=
literal|"CamelGangliaMetricName"
decl_stmt|;
DECL|field|METRIC_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|METRIC_TYPE
init|=
literal|"CamelGangliaMetricType"
decl_stmt|;
DECL|field|METRIC_SLOPE
specifier|public
specifier|static
specifier|final
name|String
name|METRIC_SLOPE
init|=
literal|"CamelGangliaMetricSlope"
decl_stmt|;
DECL|field|METRIC_UNITS
specifier|public
specifier|static
specifier|final
name|String
name|METRIC_UNITS
init|=
literal|"CamelGangliaMetricUnits"
decl_stmt|;
DECL|field|METRIC_TMAX
specifier|public
specifier|static
specifier|final
name|String
name|METRIC_TMAX
init|=
literal|"CamelGangliaMetricTmax"
decl_stmt|;
DECL|field|METRIC_DMAX
specifier|public
specifier|static
specifier|final
name|String
name|METRIC_DMAX
init|=
literal|"CamelGangliaMetricDmax"
decl_stmt|;
DECL|method|GangliaConstants ()
specifier|private
name|GangliaConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

