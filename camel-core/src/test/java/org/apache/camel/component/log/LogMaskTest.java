begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.log
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|log
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
name|ProducerTemplate
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
name|impl
operator|.
name|DefaultCamelContext
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
name|impl
operator|.
name|JndiRegistry
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
name|model
operator|.
name|Constants
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
name|MaskingFormatter
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
name|support
operator|.
name|jndi
operator|.
name|JndiTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|LogMaskTest
specifier|public
class|class
name|LogMaskTest
block|{
DECL|field|registry
specifier|protected
name|JndiRegistry
name|registry
decl_stmt|;
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|registry
operator|=
operator|new
name|JndiRegistry
argument_list|(
name|JndiTest
operator|.
name|createInitialContext
argument_list|()
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testLogMask ()
specifier|public
name|void
name|testLogMask
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setLogMask
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"log:mask?showHeaders=true"
argument_list|,
literal|"password=passw0rd@"
argument_list|,
literal|"headerPassword"
argument_list|,
literal|"#header-password$"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndProperty
argument_list|(
literal|"log:mask?showProperties=true"
argument_list|,
literal|"password=passw0rd@"
argument_list|,
literal|"propertyPassphrase"
argument_list|,
literal|"#property-passphrase$"
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDisableLogMaskViaParam ()
specifier|public
name|void
name|testDisableLogMaskViaParam
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setLogMask
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"log:mask?showHeaders=true"
argument_list|,
literal|"password=passw0rd@"
argument_list|,
literal|"headerPassword"
argument_list|,
literal|"#header-password$"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndProperty
argument_list|(
literal|"log:no-mask?showProperties=true&logMask=false"
argument_list|,
literal|"password=passw0rd@"
argument_list|,
literal|"propertyPassphrase"
argument_list|,
literal|"#property-passphrase$"
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCustomFormatter ()
specifier|public
name|void
name|testCustomFormatter
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|createCamelContext
argument_list|()
decl_stmt|;
name|MockMaskingFormatter
name|customFormatter
init|=
operator|new
name|MockMaskingFormatter
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
name|Constants
operator|.
name|CUSTOM_LOG_MASK_REF
argument_list|,
name|customFormatter
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:mock?logMask=true"
argument_list|,
literal|"password=passw0rd@"
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|customFormatter
operator|.
name|received
argument_list|,
name|customFormatter
operator|.
name|received
operator|.
name|contains
argument_list|(
literal|"password=passw0rd@"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|MockMaskingFormatter
specifier|public
specifier|static
class|class
name|MockMaskingFormatter
implements|implements
name|MaskingFormatter
block|{
DECL|field|received
specifier|private
name|String
name|received
decl_stmt|;
annotation|@
name|Override
DECL|method|format (String source)
specifier|public
name|String
name|format
parameter_list|(
name|String
name|source
parameter_list|)
block|{
name|received
operator|=
name|source
expr_stmt|;
return|return
name|source
return|;
block|}
block|}
block|}
end_class

end_unit

