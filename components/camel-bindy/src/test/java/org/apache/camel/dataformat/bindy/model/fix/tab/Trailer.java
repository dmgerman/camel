begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.model.fix.tab
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|fix
operator|.
name|tab
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
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|KeyValuePairField
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
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|Link
import|;
end_import

begin_class
annotation|@
name|Link
DECL|class|Trailer
specifier|public
class|class
name|Trailer
block|{
annotation|@
name|KeyValuePairField
argument_list|(
name|tag
operator|=
literal|10
argument_list|)
comment|// CheckSum
DECL|field|checkSum
specifier|private
name|int
name|checkSum
decl_stmt|;
DECL|method|getCheckSum ()
specifier|public
name|int
name|getCheckSum
parameter_list|()
block|{
return|return
name|checkSum
return|;
block|}
DECL|method|setCheckSum (int checkSum)
specifier|public
name|void
name|setCheckSum
parameter_list|(
name|int
name|checkSum
parameter_list|)
block|{
name|checkSum
operator|=
name|checkSum
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
return|return
literal|"Model : "
operator|+
name|Trailer
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|", "
operator|+
name|this
operator|.
name|checkSum
return|;
block|}
block|}
end_class

end_unit

