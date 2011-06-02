begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.mtom
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|mtom
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Image
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|BufferedImage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPBinding
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Assert
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
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
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * Unit test for exercising MTOM enabled end-to-end router in PAYLOAD mode  *   * @version   */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|CxfMtomPOJOProducerTest
specifier|public
class|class
name|CxfMtomPOJOProducerTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
annotation|@
name|Autowired
DECL|field|context
specifier|protected
name|CamelContext
name|context
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"http://localhost:9092/jaxws-mtom/hello"
argument_list|,
name|getImpl
argument_list|()
argument_list|)
expr_stmt|;
name|SOAPBinding
name|binding
init|=
operator|(
name|SOAPBinding
operator|)
name|endpoint
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|setMTOMEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
DECL|method|testInvokingServiceFromCxfProducer ()
specifier|public
name|void
name|testInvokingServiceFromCxfProducer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"java.awt.headless"
argument_list|)
operator|||
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Mac OS"
argument_list|)
operator|&&
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.name"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"cruise"
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Running headless. Skipping test as Images may not work."
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|photo
init|=
operator|new
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|(
name|MtomTestHelper
operator|.
name|REQ_PHOTO_DATA
argument_list|)
decl_stmt|;
specifier|final
name|Holder
argument_list|<
name|Image
argument_list|>
name|image
init|=
operator|new
name|Holder
argument_list|<
name|Image
argument_list|>
argument_list|(
name|getImage
argument_list|(
literal|"/java.jpg"
argument_list|)
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|send
argument_list|(
literal|"direct://testEndpoint"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|Object
index|[]
block|{
name|photo
block|,
name|image
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// Make sure we don't put the attachement into out message
name|assertEquals
argument_list|(
literal|"The attachement size should be 0 "
argument_list|,
literal|0
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getAttachments
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Object
index|[]
name|result
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|photo1
init|=
operator|(
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
operator|)
name|result
index|[
literal|1
index|]
decl_stmt|;
name|MtomTestHelper
operator|.
name|assertEquals
argument_list|(
name|MtomTestHelper
operator|.
name|RESP_PHOTO_DATA
argument_list|,
name|photo1
operator|.
name|value
argument_list|)
expr_stmt|;
name|Holder
argument_list|<
name|Image
argument_list|>
name|image1
init|=
operator|(
name|Holder
argument_list|<
name|Image
argument_list|>
operator|)
name|result
index|[
literal|2
index|]
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|image1
operator|.
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|image
operator|.
name|value
operator|instanceof
name|BufferedImage
condition|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|560
argument_list|,
operator|(
operator|(
name|BufferedImage
operator|)
name|image1
operator|.
name|value
operator|)
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|300
argument_list|,
operator|(
operator|(
name|BufferedImage
operator|)
name|image1
operator|.
name|value
operator|)
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getImage (String name)
specifier|private
name|Image
name|getImage
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|ImageIO
operator|.
name|read
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getImpl ()
specifier|protected
name|Object
name|getImpl
parameter_list|()
block|{
return|return
operator|new
name|HelloImpl
argument_list|()
return|;
block|}
block|}
end_class

end_unit

