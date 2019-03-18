begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.base64
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|base64
package|;
end_package

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
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
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
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|base64
operator|.
name|Base64TestConstants
operator|.
name|DECODED
import|;
end_import

begin_class
DECL|class|SpringBase64DataFormatDefaultsTest
specifier|public
class|class
name|SpringBase64DataFormatDefaultsTest
extends|extends
name|SpringBase64DataFormatTestBase
block|{
DECL|field|ENCODED
specifier|private
specifier|static
specifier|final
name|String
name|ENCODED
init|=
literal|"IrRWhNZNjFxQ6WXJEIsehbnFdurtgacAq+t6Zh3uYlyclF3HAx995mbIydQlymM8V3yA+Yb1p3Ij\r\n"
operator|+
literal|"7AS1VQaUNHAljNpHUqrWR6EmASZV/EQvR5Gk8XDvRrrtkoDm+jdZ/XKfest2OIzhixZF1mcqyi1P\r\n"
operator|+
literal|"Hep/rFnVPclO9WOWtCCRhz+U2soBzNBtvTc6x1pz1gOZcoOEFKHSf2kmkq1/7hHFl5Cb9nbSBgyp\r\n"
operator|+
literal|"lFzsInVBfCkRxXAFixwbC3B+LB8e15zSMvoG6okyDs7C8QShIZCXGHlsuUiH96izUbfB8qpTQK80\r\n"
operator|+
literal|"PPAisxYhF/gb678wvO5e/03AmFmYbBqzwoNQ6PoZKFI8a4PUrLoCLrUnKQgwOXueb1y8d4bsVGrX\r\n"
operator|+
literal|"H5QUFgAE3yZEn2ZQtVv6bZnm3lvBe/LLRD4xIU2Pcm5e+DJUZhHcl/8MaioDWFgYPLftDKvEUwLB\r\n"
operator|+
literal|"3IFWLSKMKFoeXn2nkwxsCHrzhajhbkKl1+H9I7Gkd19DyAoPIriWOJScog+mcP0iqG9iMqYFko2n\r\n"
operator|+
literal|"rh2rr+jcyKFBhrRUuNw3W8+h+FOwZDLcBmuTv2lEOvUdaPgD+1e6fXpuxhiih4wf/zlakeVa031T\r\n"
operator|+
literal|"9c0/HN02z0cAhLT1vtEA0zDn6OzzhY//Mh332ZmC+xro+e9o2a6+dnwamDtLuRgDDd+EcoUQpfEL\r\n"
operator|+
literal|"XobX3ZSX7OQw1ZXxWiJLtSOc5yLRkdbxdLK/C6fkcY4cqc/RwBGYtXN7Z1ENG/s/LnrZnRU/ErMW\r\n"
operator|+
literal|"RtbRwehA/0a2KSbNOMwK8BpzDruXufLXZcGaDKRUektQfdX4XhhYESt1drewlQLVaEWrZBR8JOd5\r\n"
operator|+
literal|"mckulPhwHp2Q00YyoScEj6Rs/9siyv49/FSaRCbnfzl3CRnNvCOD1cvF4OneYbVJCMOY49ucFmN/\r\n"
operator|+
literal|"mBCyxLOtJ4Zz8EG1FC81QTg3Scw+FdFDsCgr7DqVrmPOLikqq6wJdLBjyHXuMiVP9Fq/aAxvXEgj\r\n"
operator|+
literal|"RuVnN20wn2tUOXeaN4XqziQ66M229HsY0BX5riJ00yXArDxd+I9mFDpw/UDnGBAE2P//1fU1ns1A\r\n"
operator|+
literal|"6zQ6hTv7axdlw3/FnOAdymEKqED9CPfbiDvJygcAcxv2fyORHQ+TiprMGxckAlnLZ2pGl+gOzbtZ\r\n"
operator|+
literal|"zJgecyFJHBbhtkubGD4zzQhuJJw8ypqppSxqDs8SAW2frj42UT9qRMeCBGXLa1wyISt4GI6iOnfw\r\n"
operator|+
literal|"TCRJ/SE7CVrEfmdmROlJpAJHfUlQIJq1aW3mTE5zTmAygypxRUDCmA+eY9wdCicFp6YptdCEK3P2\r\n"
operator|+
literal|"7QzZsSASAByd5jxHMiIBkdwGzj1501xZ7hFLJDXDTQ==\r\n"
decl_stmt|;
DECL|method|SpringBase64DataFormatDefaultsTest ()
specifier|public
name|SpringBase64DataFormatDefaultsTest
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/dataformat/base64/SpringBase64DataFormatDefaultsTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testEncode ()
specifier|public
name|void
name|testEncode
parameter_list|()
throws|throws
name|Exception
block|{
name|runEncoderTest
argument_list|(
name|DECODED
argument_list|,
name|ENCODED
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDecode ()
specifier|public
name|void
name|testDecode
parameter_list|()
throws|throws
name|Exception
block|{
name|runDecoderTest
argument_list|(
name|ENCODED
operator|.
name|getBytes
argument_list|()
argument_list|,
name|DECODED
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

