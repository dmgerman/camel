begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.crypto
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|crypto
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"It looks like the bcpg dosen't work well with the JDK of CI"
operator|+
literal|"The tests are passed in my Mac JDK-1.6.0_33"
argument_list|)
DECL|class|PGPDataFormatElGamalTest
specifier|public
class|class
name|PGPDataFormatElGamalTest
extends|extends
name|PGPDataFormatTest
block|{
DECL|method|getKeyFileName ()
specifier|protected
name|String
name|getKeyFileName
parameter_list|()
block|{
return|return
literal|"org/apache/camel/component/crypto/pubring-ElGamal.gpg"
return|;
block|}
DECL|method|getKeyFileNameSec ()
specifier|protected
name|String
name|getKeyFileNameSec
parameter_list|()
block|{
return|return
literal|"org/apache/camel/component/crypto/secring-ElGamal.gpg"
return|;
block|}
block|}
end_class

end_unit

