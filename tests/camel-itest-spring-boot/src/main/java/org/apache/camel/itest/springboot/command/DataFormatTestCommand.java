begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.springboot.command
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|springboot
operator|.
name|command
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|itest
operator|.
name|springboot
operator|.
name|Command
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
name|itest
operator|.
name|springboot
operator|.
name|ITestConfig
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
name|spi
operator|.
name|DataFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_comment
comment|/**  * A command that tries to lookup a camel data-format.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"dataformat"
argument_list|)
DECL|class|DataFormatTestCommand
specifier|public
class|class
name|DataFormatTestCommand
extends|extends
name|AbstractTestCommand
implements|implements
name|Command
block|{
DECL|field|logger
specifier|private
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Autowired
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Override
DECL|method|executeTest (ITestConfig config, String dataFormat)
specifier|public
name|Boolean
name|executeTest
parameter_list|(
name|ITestConfig
name|config
parameter_list|,
name|String
name|dataFormat
parameter_list|)
throws|throws
name|Exception
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Getting Camel dataFormat: {}"
argument_list|,
name|dataFormat
argument_list|)
expr_stmt|;
name|DataFormat
name|df
init|=
name|context
operator|.
name|resolveDataFormat
argument_list|(
name|dataFormat
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Cannot get dataformat with name: "
operator|+
name|dataFormat
argument_list|,
name|df
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Found Camel dataformat: {} instance: {} with className: {}"
argument_list|,
name|dataFormat
argument_list|,
name|df
argument_list|,
name|df
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

