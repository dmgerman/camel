begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_class
DECL|class|Poetry
specifier|public
class|class
name|Poetry
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|poet
specifier|private
name|String
name|poet
init|=
literal|"?"
decl_stmt|;
DECL|field|poem
specifier|private
name|String
name|poem
init|=
literal|"ONCE in the dream of a night I stood\n"
operator|+
literal|"Lone in the light of a magical wood,\n"
operator|+
literal|"Soul-deep in visions that poppy-like sprang;\n"
operator|+
literal|"And spirits of Truth were the birds that sang,\n"
operator|+
literal|"And spirits of Love were the stars that glowed,\n"
operator|+
literal|"And spirits of Peace were the streams that flowed\n"
operator|+
literal|"In that magical wood in the land of sleep."
operator|+
literal|"\n"
operator|+
literal|"Lone in the light of that magical grove,\n"
operator|+
literal|"I felt the stars of the spirits of Love\n"
operator|+
literal|"Gather and gleam round my delicate youth,\n"
operator|+
literal|"And I heard the song of the spirits of Truth;\n"
operator|+
literal|"To quench my longing I bent me low\n"
operator|+
literal|"By the streams of the spirits of Peace that flow\n"
operator|+
literal|"In that magical wood in the land of sleep."
decl_stmt|;
DECL|method|getPoet ()
specifier|public
name|String
name|getPoet
parameter_list|()
block|{
return|return
name|poet
return|;
block|}
DECL|method|setPoet (String poet)
specifier|public
name|void
name|setPoet
parameter_list|(
name|String
name|poet
parameter_list|)
block|{
name|this
operator|.
name|poet
operator|=
name|poet
expr_stmt|;
block|}
DECL|method|getPoem ()
specifier|public
name|String
name|getPoem
parameter_list|()
block|{
return|return
name|poem
return|;
block|}
DECL|method|setPoem (String poem)
specifier|public
name|void
name|setPoem
parameter_list|(
name|String
name|poem
parameter_list|)
block|{
name|this
operator|.
name|poem
operator|=
name|poem
expr_stmt|;
block|}
block|}
end_class

end_unit

