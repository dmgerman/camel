begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.gae
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|gae
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|Exchange
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
name|Processor
import|;
end_import

begin_class
DECL|class|ReportGenerator
specifier|public
class|class
name|ReportGenerator
implements|implements
name|Processor
block|{
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|ReportData
name|data
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ReportData
operator|.
name|class
argument_list|)
decl_stmt|;
name|XPathFactory
name|xpfactory
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|XPath
name|xpath
init|=
name|xpfactory
operator|.
name|newXPath
argument_list|()
decl_stmt|;
comment|// Extract result values via XPath
name|String
name|city
init|=
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"//forecast_information/city/@data"
argument_list|,
name|data
operator|.
name|getWeather
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|cond
init|=
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"//current_conditions/condition/@data"
argument_list|,
name|data
operator|.
name|getWeather
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|temp
init|=
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"//current_conditions/temp_c/@data"
argument_list|,
name|data
operator|.
name|getWeather
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|city
operator|==
literal|null
operator|||
name|city
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|city
operator|=
name|data
operator|.
name|getCity
argument_list|()
expr_stmt|;
name|cond
operator|=
literal|"<error retrieving current condition>"
expr_stmt|;
name|temp
operator|=
literal|"<error retrieving current temperature>"
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|append
argument_list|(
literal|"Weather report for:  "
argument_list|)
operator|.
name|append
argument_list|(
name|city
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|append
argument_list|(
literal|"Current condition:   "
argument_list|)
operator|.
name|append
argument_list|(
name|cond
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|append
argument_list|(
literal|"Current temperature: "
argument_list|)
operator|.
name|append
argument_list|(
name|temp
argument_list|)
operator|.
name|append
argument_list|(
literal|" (Celsius)"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

