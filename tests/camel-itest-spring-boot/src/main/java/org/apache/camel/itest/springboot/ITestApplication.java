begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.springboot
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|ch
operator|.
name|qos
operator|.
name|logback
operator|.
name|classic
operator|.
name|LoggerContext
import|;
end_import

begin_import
import|import
name|ch
operator|.
name|qos
operator|.
name|logback
operator|.
name|classic
operator|.
name|joran
operator|.
name|JoranConfigurator
import|;
end_import

begin_import
import|import
name|ch
operator|.
name|qos
operator|.
name|logback
operator|.
name|core
operator|.
name|joran
operator|.
name|spi
operator|.
name|JoranException
import|;
end_import

begin_import
import|import
name|ch
operator|.
name|qos
operator|.
name|logback
operator|.
name|core
operator|.
name|util
operator|.
name|StatusPrinter
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
name|boot
operator|.
name|SpringApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|SpringBootApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Import
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|scheduling
operator|.
name|annotation
operator|.
name|EnableAsync
import|;
end_import

begin_comment
comment|/**  * Contains the main class of the sample spring-boot application created for the   * module under test.  *  */
end_comment

begin_class
annotation|@
name|SpringBootApplication
annotation|@
name|EnableAsync
annotation|@
name|Import
argument_list|(
name|ITestXmlConfiguration
operator|.
name|class
argument_list|)
DECL|class|ITestApplication
specifier|public
class|class
name|ITestApplication
block|{
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|overrideLoggingConfig
argument_list|()
expr_stmt|;
name|SpringApplication
operator|.
name|run
argument_list|(
name|ITestApplication
operator|.
name|class
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
comment|// to tell source-check this is not a utility-class
return|return
literal|"spring-boot-main"
return|;
block|}
DECL|method|overrideLoggingConfig ()
specifier|private
specifier|static
name|void
name|overrideLoggingConfig
parameter_list|()
block|{
name|URL
name|logbackFile
init|=
name|ITestApplication
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/spring-logback.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
name|logbackFile
operator|!=
literal|null
condition|)
block|{
name|LoggerContext
name|context
init|=
operator|(
name|LoggerContext
operator|)
name|LoggerFactory
operator|.
name|getILoggerFactory
argument_list|()
decl_stmt|;
try|try
block|{
name|JoranConfigurator
name|configurator
init|=
operator|new
name|JoranConfigurator
argument_list|()
decl_stmt|;
name|configurator
operator|.
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
comment|// Call context.reset() to clear any previous configuration, e.g. default
comment|// configuration. For multi-step configuration, omit calling context.reset().
name|context
operator|.
name|reset
argument_list|()
expr_stmt|;
name|configurator
operator|.
name|doConfigure
argument_list|(
name|logbackFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JoranException
name|je
parameter_list|)
block|{
comment|// StatusPrinter will handle this
block|}
name|StatusPrinter
operator|.
name|printInCaseOfErrorsOrWarnings
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

