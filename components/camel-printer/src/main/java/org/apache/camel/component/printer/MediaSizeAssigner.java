begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.printer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|printer
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|standard
operator|.
name|MediaSizeName
import|;
end_import

begin_class
DECL|class|MediaSizeAssigner
specifier|public
class|class
name|MediaSizeAssigner
block|{
DECL|field|mediaSizeName
specifier|private
name|MediaSizeName
name|mediaSizeName
decl_stmt|;
DECL|method|selectMediaSizeNameISO (String size)
specifier|public
name|MediaSizeName
name|selectMediaSizeNameISO
parameter_list|(
name|String
name|size
parameter_list|)
block|{
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_a0"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_A0
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_a1"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_A1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_a2"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_A2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_a3"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_A3
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_a4"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_A4
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_a5"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_A5
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_a6"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_A6
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_a7"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_A7
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_a8"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_A8
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_a9"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_A9
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_a10"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_A10
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_b0"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_B0
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_b1"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_B1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_b2"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_B2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_b3"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_B3
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_b4"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_B4
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_b5"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_B5
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_b6"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_B6
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_b7"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_B7
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_b8"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_B8
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_b9"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_B9
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_b10"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_B10
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_c0"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_C0
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_c1"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_C1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_c2"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_C2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_c3"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_C3
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_c4"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_C4
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_c5"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_C5
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_c6"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_C6
expr_stmt|;
block|}
return|return
name|mediaSizeName
return|;
block|}
DECL|method|selectMediaSizeNameJIS (String size)
specifier|public
name|MediaSizeName
name|selectMediaSizeNameJIS
parameter_list|(
name|String
name|size
parameter_list|)
block|{
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"jis_b0"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|JIS_B0
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"jis_b1"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|JIS_B1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"jis_b2"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|JIS_B2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"jis_b3"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|JIS_B3
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"jis_b4"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|JIS_B4
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"jis_b5"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|JIS_B5
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"jis_b6"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|JIS_B6
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"jis_b7"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|JIS_B7
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"jis_b8"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|JIS_B8
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"jis_b9"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|JIS_B9
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"jis_b10"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|JIS_B10
expr_stmt|;
block|}
return|return
name|mediaSizeName
return|;
block|}
DECL|method|selectMediaSizeNameNA (String size)
specifier|public
name|MediaSizeName
name|selectMediaSizeNameNA
parameter_list|(
name|String
name|size
parameter_list|)
block|{
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_letter"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_LETTER
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_legal"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_LEGAL
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"executive"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|EXECUTIVE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"ledger"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|LEDGER
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"tabloid"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|TABLOID
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"invoice"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|INVOICE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"folio"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|FOLIO
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"quarto"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|QUARTO
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"japanese_postcard"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|JAPANESE_POSTCARD
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"oufuko_postcard"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|JAPANESE_DOUBLE_POSTCARD
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"a"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|A
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"b"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|B
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"c"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|C
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"d"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|D
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"e"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|E
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_designated_long"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_DESIGNATED_LONG
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"italian_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ITALY_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"monarch_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|MONARCH_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"personal_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|PERSONAL_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_number_9_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_NUMBER_9_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_number_10_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_NUMBER_10_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_number_11_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_NUMBER_11_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_number_12_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_NUMBER_12_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_number_14_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_NUMBER_14_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_6x9_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_6X9_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_7x9_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_7X9_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_9x11_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_9X11_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_9x12_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_9X12_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_10x13_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_10X13_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_10x14_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_10X14_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_10x15_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_10X15_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_5x7"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_5X7
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"na_8x10"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_8X10
expr_stmt|;
block|}
else|else
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|NA_LETTER
expr_stmt|;
block|}
return|return
name|mediaSizeName
return|;
block|}
DECL|method|selectMediaSizeNameOther (String size)
specifier|public
name|MediaSizeName
name|selectMediaSizeNameOther
parameter_list|(
name|String
name|size
parameter_list|)
block|{
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"executive"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|EXECUTIVE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"ledger"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|LEDGER
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"tabloid"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|TABLOID
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"invoice"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|INVOICE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"folio"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|FOLIO
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"quarto"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|QUARTO
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"japanese_postcard"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|JAPANESE_POSTCARD
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"oufuko_postcard"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|JAPANESE_DOUBLE_POSTCARD
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"a"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|A
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"b"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|B
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"c"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|C
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"d"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|D
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"e"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|E
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"iso_designated_long"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ISO_DESIGNATED_LONG
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"italian_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|ITALY_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"monarch_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|MONARCH_ENVELOPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"personal_envelope"
argument_list|)
condition|)
block|{
name|mediaSizeName
operator|=
name|MediaSizeName
operator|.
name|PERSONAL_ENVELOPE
expr_stmt|;
block|}
return|return
name|mediaSizeName
return|;
block|}
block|}
end_class

end_unit

